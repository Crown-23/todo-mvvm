package com.crown.todo.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.crown.todo.ScrollChildSwipeRefreshLayout
import com.crown.todo.TodoApplication
import com.crown.todo.ViewModelFactory

/**
 * Description：Extension functions for Fragment.
 */
fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TodoApplication).tasksRepository
    return ViewModelFactory(repository, this)
}

fun Fragment.setupRefreshLayout(
    refreshLayout: ScrollChildSwipeRefreshLayout,
    scrollUpChild: View? = null
) {
    // Set the scrolling view in the custom SwipeRefreshLayout.
    scrollUpChild?.let {
        refreshLayout.scrollUpChild = it
    }
}