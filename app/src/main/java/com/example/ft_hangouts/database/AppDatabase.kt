package com.example.ft_hangouts.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var _instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (_instance == null) {
                _instance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "ft_hangouts")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return _instance!!
        }
    }
}
