package org.example.project.data.repository

import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.data.api.AuthApi
import org.example.project.data.api.CabinetApi
import org.example.project.data.api.Networking
import org.example.project.data.models.RemoteCabinet
import org.example.project.data.models.RemoteLoginRequest
import org.example.project.data.models.RemoteProject
import org.example.project.data.models.RemoteUserData
import org.example.project.domain.models.AuthState
import org.example.project.domain.models.Cabinet
import org.example.project.domain.models.Project
import org.example.project.domain.models.User

class AuthRepository(
    private val settings: Settings
) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthorized)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentCabinet = MutableStateFlow<Cabinet?>(null)
    val currentCabinet: StateFlow<Cabinet?> = _currentCabinet.asStateFlow()

    private val _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject.asStateFlow()

    private var currentDomain: String? = null
    private lateinit var currentHttpClient: HttpClient

    suspend fun login(domain: String, email: String, password: String): Result<User> {
        return runCatching {
            _authState.value = AuthState.Loading(true)

            currentDomain = domain
            currentHttpClient = Networking.createHttpClient(domain)
            val authApi = AuthApi(currentHttpClient)

            val request = RemoteLoginRequest(email, password)
            val response = authApi.login(domain, request)

            when {
                response.status == "ok" && response.data != null -> {
                    saveDomain(domain)
                    val user = response.data.toDomain()

                    val cabinets = loadCabinets()
                    user.copy(cabinets = cabinets).also {
                        _authState.value = AuthState.Authorized
                    }
                }

                else -> {
                    val error = response.message ?: "Login failed"
                    _authState.value = AuthState.Error(error)
                    throw Exception(error)
                }
            }
        }
    }

    private suspend fun loadCabinets(): List<Cabinet> {
        return runCatching {
            val cabinetApi = CabinetApi(currentHttpClient)
            val response = cabinetApi.getCabinets(currentDomain!!)

            response.data?.map { it.toDomain() } ?: emptyList()
        }.getOrDefault(emptyList())
    }

    suspend fun selectCabinet(cabinetId: String): Result<Unit> {
        return runCatching {
            val cabinets = loadCabinets()
            val cabinet = cabinets.firstOrNull { it.id == cabinetId }
                ?: throw Exception("Cabinet not found")

            _currentCabinet.value = cabinet
            saveCurrentCabinetId(cabinetId)

            currentHttpClient = Networking.createHttpClient(
                domain = currentDomain!!,
                cabinetId = cabinetId,
                projectId = _currentProject.value?.id
            )

            loadProjectsForCabinet(cabinetId)
        }
    }

    private suspend fun loadProjectsForCabinet(cabinetId: String) {
        runCatching {
            val cabinetApi = CabinetApi(currentHttpClient)
            val response = cabinetApi.getProjects(currentDomain!!, cabinetId)

            response.data?.let { projects ->
                val cabinetWithProjects = _currentCabinet.value?.copy(
                    projects = projects.map { it.toDomain() }
                )
                _currentCabinet.value = cabinetWithProjects
            }
        }.onFailure {
            Napier.e("Failed to load projects", it)
        }
    }

    suspend fun selectProject(projectId: String): Result<Unit> {
        return runCatching {
            val project = _currentCabinet.value?.projects?.firstOrNull { it.id == projectId }
                ?: throw Exception("Project not found")

            _currentProject.value = project
            saveCurrentProjectId(projectId)

            currentHttpClient = Networking.createHttpClient(
                domain = currentDomain!!,
                cabinetId = _currentCabinet.value?.id,
                projectId = projectId
            )
        }
    }

    suspend fun logout() {
        runCatching {
            if (::currentHttpClient.isInitialized) {
                val authApi = AuthApi(currentHttpClient)
                authApi.logout(currentDomain!!)
            }
        }.onFailure {
            Napier.e("Logout error", it)
        }

        clearAllData()
        Networking.clearCookies()
        _authState.value = AuthState.Unauthorized
        _currentCabinet.value = null
        _currentProject.value = null
        currentDomain = null
    }

    suspend fun checkAuthState(): AuthState {
        val domain = getDomain()
        val cabinetId = getCurrentCabinetId()
        val projectId = getCurrentProjectId()

        return if (domain != null) {
            currentDomain = domain
            currentHttpClient = Networking.createHttpClient(
                domain = domain,
                cabinetId = cabinetId,
                projectId = projectId
            )

            val cabinetsResult = runCatching { loadCabinets() }

            if (cabinetsResult.isSuccess) {
                val cabinets = cabinetsResult.getOrDefault(emptyList())

                if (cabinetId != null) {
                    _currentCabinet.value = cabinets.firstOrNull { it.id == cabinetId }
                }

                AuthState.Authorized
            } else {
                clearAllData()
                AuthState.Unauthorized
            }
        } else {
            AuthState.Unauthorized
        }.also {
            _authState.value = it
        }
    }

    fun observeCabinet(): Flow<Cabinet?> = _currentCabinet.asStateFlow()

    fun observeProject(): Flow<Project?> = _currentProject.asStateFlow()

    fun getRequiredHeaders(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        getCurrentCabinetId()?.let {
            headers["X-SPro-Cabinet"] = it
        }
        getCurrentProjectId()?.let {
            headers["X-SPro-Project"] = it
        }
        return headers
    }

    private fun saveDomain(domain: String) {
        settings.putString("domain", domain)
    }

    fun getDomain(): String? = settings.getStringOrNull("domain")

    private fun saveCurrentCabinetId(id: String) {
        settings.putString("current_cabinet_id", id)
    }

    fun getCurrentCabinetId(): String? = settings.getStringOrNull("current_cabinet_id")

    private fun saveCurrentProjectId(id: String) {
        settings.putString("current_project_id", id)
    }

    fun getCurrentProjectId(): String? = settings.getStringOrNull("current_project_id")

    private fun clearAllData() {
        settings.clear()
    }
}

private fun RemoteUserData.toDomain(): User = User(
    id = id,
    email = email,
    name = name,
    cabinets = cabinets?.map { it.toDomain() } ?: emptyList()
)

private fun RemoteCabinet.toDomain(): Cabinet = Cabinet(
    id = id,
    name = name,
    domain = domain,
    projects = projects?.map { it.toDomain() } ?: emptyList()
)

private fun RemoteProject.toDomain(): Project = Project(
    id = id,
    name = name,
    cabinetId = id // Временно
)