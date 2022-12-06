package com.example.goodhabits.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goodhabits.data.source.local.entity.Todo

@Database(version = 1, entities = [Todo::class])
abstract class TodoDb : RoomDatabase() {

    abstract fun todoDAO(): TodoDAO

    companion object { // singleton
        @Volatile
        private var INSTANCE: TodoDb? = null

        fun getInstance(context: Context): TodoDb {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDb::class.java,
                        "todo_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}