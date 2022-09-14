package com.crown.todo.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.crown.todo.R
import com.crown.todo.databinding.FragmentStatisticsBinding
import com.crown.todo.utils.getViewModelFactory
import com.crown.todo.utils.setupRefreshLayout

/**
 * Description：Main UI for the statistics screen.
 */
class StatisticsFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentStatisticsBinding

    private val viewModel by viewModels<StatisticsViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewmodel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        setupRefreshLayout(viewDataBinding.refreshLayout)

        viewDataBinding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}

