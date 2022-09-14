package com.crown.todo.tasks

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crown.todo.EventObserver
import com.crown.todo.R
import com.crown.todo.databinding.FragmentTasksBinding
import com.crown.todo.utils.getViewModelFactory
import com.crown.todo.utils.setupRefreshLayout
import com.crown.todo.utils.setupSnackbar
import com.google.android.material.snackbar.Snackbar

/**
 * Description：
 */
class TasksFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTasksBinding

    private lateinit var listAdapter: TasksAdapter

    private val viewModel by viewModels<TasksViewModel> { getViewModelFactory() }

    private val args: TasksFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            R.id.menu_clear -> {
                viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_refresh -> {
                viewModel.loadTasks(true)
                true
            }
            else -> false
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.tasksList)
        setupNavigation()
        setupFab()

        viewDataBinding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupSnackbar() {
        viewDataBinding.root.setupSnackbar(
            viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_SHORT
        )
        arguments?.let {
            viewModel.showEditResultMessage(args.userMessage)
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = TasksAdapter(viewModel)
            viewDataBinding.tasksList.adapter = listAdapter
        }
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver {
            openTaskDetails(it)
        })
    }

    private fun setupFab() {
        viewDataBinding.addTaskFab.setOnClickListener {
            navigateToAddNewTask()
        }
    }

    private fun navigateToAddNewTask() {
        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
            null,
            resources.getString(R.string.add_task)
        )
        findNavController().navigate(action)
    }

    private fun openTaskDetails(taskId: String) {
        val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }

    private fun showFilteringPopUpMenu() {
        val view = requireActivity().findViewById<View>(R.id.menu_filter)
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks_menu, menu)

            setOnMenuItemClickListener {
                viewModel.setFiltering(
                    when (it.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                true
            }

            show()
        }
    }


}

