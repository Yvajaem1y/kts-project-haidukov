package org.example.project.data.local_database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.data.local_database.room.AppDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/my_room.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory =  { AppDatabase::class as AppDatabase }
    )
}

fun getDatabase(): AppDatabase {
    return getDatabaseBuilder().build()
}