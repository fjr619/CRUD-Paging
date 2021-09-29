package com.fjr.crudpagging


/**
 * Created by Franky Wijanarko on 28/09/21.
 * frank.jr.619@gmail.com
 */
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect

class SampleFragment : Fragment(R.layout.sample_fragment) {
    private val viewModel: SampleViewModel by viewModels()
    private val sampleAdapter by lazy { SampleAdapter(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvItems = view.findViewById<RecyclerView>(R.id.rvItems)
        rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sampleAdapter.withLoadStateFooter(DefaultLoadStateAdapter())
        }

        view.findViewById<View>(R.id.btInsertHeaderItem).setOnClickListener {
            viewModel.onViewEvent(SampleViewEvents.InsertItemHeader)
        }

        view.findViewById<View>(R.id.btInsertFooterItem).setOnClickListener {
            viewModel.onViewEvent(SampleViewEvents.InsertItemFooter)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pagingFlow.collect { pagingData ->
                sampleAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData!!)
            }
        }

//        viewModel.pagingDataViewStates.observe(viewLifecycleOwner, Observer { pagingData ->
//            sampleAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
//        })
    }
}
