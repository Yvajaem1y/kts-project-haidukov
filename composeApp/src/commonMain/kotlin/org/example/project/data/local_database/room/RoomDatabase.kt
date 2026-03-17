package org.example.project.data.local_database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.project.data.local_database.room.models.RepositoryPreviewDBO
import org.example.project.data.local_database.room.roomDAO.RoomDAO

@Database(entities = [RepositoryPreviewDBO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): RoomDAO
}
