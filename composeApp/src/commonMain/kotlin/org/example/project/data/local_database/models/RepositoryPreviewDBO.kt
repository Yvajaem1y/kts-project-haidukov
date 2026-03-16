package org.example.project.data.local_database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "repositories_table")
data class RepositoryPreviewDBO (
    @PrimaryKey(autoGenerate = false) val id : Long,
    val repositoryName: String,
    val description: String? = null,
    val language: String? = null,
    val starsCount: Int,
    @Embedded val owner: OwnerDBO,
    val lastUpdated: Long
)

@Serializable
data class OwnerDBO(
    val login: String,
    val avatarUrl: String? = null,
)