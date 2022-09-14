package com.crown.todo.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crown.todo.EventObserver
import com.crown.todo.databinding.FragmentAddTaskBinding
import com.crown.todo.tasks.ADD_EDIT_RESULT_OK
import com.crown.todo.utils.getViewModelFactory
import com.crown.todo.utils.setupSnackbar
import com.google.android.material.snackbar.Snackbar

/**
 * Description：Main UI for the add task screen. Users can enter a task title and description.
 */
class AddEditTaskFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentAddTaskBinding

    private val args: AddEditTaskFragmentArgs by navArgs()

    private val viewModel by viewModels<AddEditTaskViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentAddTaskBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        setupNavigation()
        viewModel.start(args.taskId)
    }

    private fun setupSnackbar() {
        viewDataBinding.root.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToTasksFragment(
                ADD_EDIT_RESULT_OK
            )
            findNavController().navigate(action)
        })
    }


}

