package com.crown.todo

import android.content.Context
import androidx.room.Room
import com.crown.todo.data.FakeTasksRemoteDataSource
import com.crown.todo.data.source.DefaultTasksRepository
import com.crown.todo.data.source.TasksDataSource
import com.crown.todo.data.source.TasksRepository
import com.crown.todo.data.source.local.TasksLocalDataSource
import com.crown.todo.data.source.local.ToDoDatabase

/**
 * Description：
 */
object ServiceLocator {
    private var database: ToDoDatabase? = null

    @Volatile
    var tasksRepository: TasksRepository? = null

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo = DefaultTasksRepository(FakeTasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(
        context: Context,
        inMemory: Boolean = false
    ): ToDoDatabase {
        val result = if (inMemory) {
            // Use a faster in-memory database for tests
            Room.inMemoryDatabaseBuilder(context.applicationContext, ToDoDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        } else {
            // Real database using SQLite
            Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java, "Tasks.db")
                .build()
        }
        database = result
        return result
    }

}



