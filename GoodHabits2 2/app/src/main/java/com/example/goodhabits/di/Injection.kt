package com.example.goodhabits.di

import android.content.Context
import com.example.goodhabits.data.repository.TodoRepository
import com.example.goodhabits.data.source.local.LocalDataSource
import com.example.goodhabits.data.source.local.room.TodoDb

object Injection {

    fun provideRepository(context: Context): TodoRepository {
        val database = TodoDb.getInstance(context)

        val localDataSource = LocalDataSource.getInstance(database.todoDAO())

        return TodoRepository.getInstance(localDataSource)
    }
}