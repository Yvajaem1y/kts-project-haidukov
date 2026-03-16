package org.example.project.presentation.repositoriesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.ic_empty_photo
import kts_project_haidukov.composeapp.generated.resources.load_more
import kts_project_haidukov.composeapp.generated.resources.no_repositories_description
import kts_project_haidukov.composeapp.generated.resources.no_repositories_title
import kts_project_haidukov.composeapp.generated.resources.retry
import kts_project_haidukov.composeapp.generated.resources.search_placeholder
import kts_project_haidukov.composeapp.generated.resources.unknown_error
import kts_project_haidukov.composeapp.generated.resources.user_avatar
import org.example.project.domain.models.RepositoryPreview
import org.example.project.presentation.common.GitHubColors
import org.example.project.presentation.common.GitHubTheme
import org.example.project.presentation.common.RequestResponseUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepositoriesScreen() {
    val viewModel: RepositoriesScreenViewModel = koinViewModel()
    val currentUiState by viewModel.state.collectAsState()
    val colors = GitHubTheme.colors
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        TextField(
            value = currentUiState.query,
            onValueChange = { viewModel.updateQueryText(it) },
            placeholder = {
                Text(
                    text = stringResource(Res.string.search_placeholder),
                    color = colors.textTertiary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textSecondary,
                focusedContainerColor = colors.backgroundSecondary,
                unfocusedContainerColor = colors.backgroundSecondary,
                focusedIndicatorColor = colors.primary,
                unfocusedIndicatorColor = colors.borderPrimary,
                cursorColor = colors.primary,
                focusedPlaceholderColor = colors.textTertiary,
                unfocusedPlaceholderColor = colors.textTertiary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        when (val curState = currentUiState.requestResultWithRepositories) {
            is RequestResponseUiState.InProgress -> {
                InProgressScreen(
                    list = curState.item,
                    colors = colors,
                    scrollState = scrollState
                )
            }

            RequestResponseUiState.None -> {
                EmptyScreen(colors = colors)
            }

            is RequestResponseUiState.OnError -> {
                OnFailureLoadingScreen(
                    list = curState.item,
                    error = curState.error,
                    colors = colors,
                    onRetry = { viewModel.getInitialRepositories() },
                    scrollState = scrollState
                )
            }

            is RequestResponseUiState.OnSuccess -> {
                OnSuccessLoadingScreen(
                    list = curState.item,
                    onLoadMore = { viewModel.searchNewRepositories() },
                    colors = colors,
                    scrollState = scrollState
                )
            }
        }
    }
}

@Composable
private fun OnSuccessLoadingScreen(
    list: ImmutableList<RepositoryPreview>,
    onLoadMore: () -> Unit,
    colors: GitHubColors,
    scrollState: LazyListState
) {
    RepositoriesListScreen(
        list = list,
        colors = colors,
        scrollState = scrollState,
        footerContent = {
            Button(
                onClick = onLoadMore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(Res.string.load_more))
            }
        }
    )
}

@Composable
private fun InProgressScreen(
    list: ImmutableList<RepositoryPreview>,
    colors: GitHubColors,
    scrollState: LazyListState
) {
    RepositoriesListScreen(
        list = list,
        colors = colors,
        scrollState = scrollState,
        footerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colors.primary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    )
}

@Composable
private fun OnFailureLoadingScreen(
    list: ImmutableList<RepositoryPreview>?,
    error: Throwable,
    colors: GitHubColors,
    onRetry: () -> Unit,
    scrollState: LazyListState
) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            color = colors.danger.copy(alpha = 0.1f),
            tonalElevation = 0.dp,
        ) {
            Text(
                text = error.message ?: stringResource(Res.string.unknown_error),
                color = colors.danger,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (list != null) {
            RepositoriesListScreen(
                list = list,
                colors = colors,
                scrollState = scrollState,
                footerContent = {
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.textOnColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(stringResource(Res.string.retry))
                    }
                }
            )
        }
    }
}

@Composable
private fun EmptyScreen(
    colors: GitHubColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.no_repositories_title),
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.no_repositories_description),
            color = colors.textSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun RepositoriesListScreen(
    list: ImmutableList<RepositoryPreview>,
    colors: GitHubColors,
    footerContent: @Composable () -> Unit,
    scrollState: LazyListState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = scrollState
    ) {
        items(
            items = list,
            key = { "${it.repositoryName}_${it.owner}" }
        ) { item ->
            RepositoryPreviewItem(
                repositoryPreview = item,
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Navigate to repository details */ }
            )
        }

        item {
            footerContent()
        }
    }
}

@Composable
private fun RepositoryPreviewItem(
    repositoryPreview: RepositoryPreview,
    colors: GitHubColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        color = colors.backgroundSecondary,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = repositoryPreview.owner.avatarUrl,
                        contentDescription = stringResource(Res.string.user_avatar),
                        placeholder = painterResource(Res.drawable.ic_empty_photo),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${repositoryPreview.owner.login}/${repositoryPreview.repositoryName}",
                        color = colors.textLink,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                repositoryPreview.description.takeIf { it.isNotBlank() }?.let { description ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(repositoryPreview.language, colors))
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = repositoryPreview.language,
                        color = getLanguageColor(repositoryPreview.language, colors),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (repositoryPreview.starsCount > 0) {
                Text(
                    text = "★ ${repositoryPreview.starsCount}",
                    color = colors.textTertiary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private fun getLanguageColor(
    language: String?,
    colors: GitHubColors
): Color {
    return when (language?.lowercase()) {
        "kotlin" -> colors.languageKotlin
        "java" -> colors.languageJava
        "javascript", "typescript", "js", "ts" -> colors.languageJavaScript
        "python", "py" -> colors.languagePython
        "swift" -> colors.languageSwift
        "go", "golang" -> colors.languageGo
        "rust", "rs" -> colors.languageRust
        "c++", "c", "cpp", "cplusplus" -> colors.languageCpp
        else -> colors.languageDefault
    }
}