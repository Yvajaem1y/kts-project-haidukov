package org.example.project.data.local_database.room.roomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.local_database.room.models.RepositoryPreviewDBO

@Dao
interface RoomDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun invokeRepositoryPreview(listItems: List<RepositoryPreviewDBO>)

    @Query("SELECT * FROM repositories_table WHERE repositoryName LIKE '%' || :query || '%'")
    fun searchRepositoriesByName(query: String): Flow<List<RepositoryPreviewDBO>>

    @Query("SELECT * FROM repositories_table WHERE repositoryName LIKE '%' || :query || '%'")
    suspend fun searchRepositoriesByNameOneTime(query: String): List<RepositoryPreviewDBO>
}