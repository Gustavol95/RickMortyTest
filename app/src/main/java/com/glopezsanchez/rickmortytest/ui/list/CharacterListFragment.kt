package com.glopezsanchez.rickmortytest.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.glopezsanchez.rickmortytest.databinding.FragmentCharacterListBinding
import com.glopezsanchez.rickmortytest.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterListFragment : Fragment() {

    private lateinit var binding: FragmentCharacterListBinding
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()
    private val adapter: CharacterAdapter =
        CharacterAdapter { item ->  }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCharacters.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listUiState.collectLatest { state ->
                if (state.data != null) {
                    adapter.submitData(state.data)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                handleLoadingState(it)
            }
        }
    }

    private fun handleLoadingState(loadStates: CombinedLoadStates) {
        binding.loadingContainer.isVisible =
            (loadStates.refresh is LoadState.Loading) || (loadStates.append is LoadState.Loading)
        if ((loadStates.refresh is LoadState.Error) || (loadStates.append is LoadState.Error)) {
            Toast.makeText(
                requireContext(),
                "We're having trouble loading the data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}