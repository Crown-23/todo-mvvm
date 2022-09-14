package com.crown.todo.taskdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crown.todo.EventObserver
import com.crown.todo.R
import com.crown.todo.databinding.FragmentTaskDetailBinding
import com.crown.todo.tasks.DELETE_RESULT_OK
import com.crown.todo.utils.getViewModelFactory
import com.crown.todo.utils.setupSnackbar
import com.google.android.material.snackbar.Snackbar

/**
 * Description：Main UI for the task detail screen.
 */
class TaskDetailFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTaskDetailBinding

    private val args: TaskDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<TaskDetailViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentTaskDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        viewModel.start(args.taskId)

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFab()
        view.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        setupNavigation()
    }

    private fun setupFab() {
        viewDataBinding.editTaskFab.setOnClickListener {
            viewModel.editTask()
        }
    }

    private fun setupNavigation() {
        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTasksFragment(
                DELETE_RESULT_OK
            )
            findNavController().navigate(action)
        })
        viewModel.editTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToAddEditTaskFragment(
                args.taskId,
                resources.getString(R.string.edit_task)
            )
            findNavController().navigate(action)
        })
    }


}

