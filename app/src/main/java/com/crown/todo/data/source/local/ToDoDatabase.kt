package com.crown.todo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crown.todo.data.Task

/**
 * Description：The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    // TODO: [hub] 没有实现。用处是什么？
    abstract fun taskDao(): TasksDao

}