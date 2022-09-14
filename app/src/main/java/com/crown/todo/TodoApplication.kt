package com.crown.todo

import android.app.Application
import com.crown.todo.data.source.TasksRepository

/**
 * Description：An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class TodoApplication : Application() {
    val tasksRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)
}