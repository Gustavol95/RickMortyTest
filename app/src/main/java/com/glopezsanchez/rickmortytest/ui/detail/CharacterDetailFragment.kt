package com.glopezsanchez.rickmortytest.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.glopezsanchez.rickmortytest.R
import com.glopezsanchez.rickmortytest.databinding.FragmentCharacterDetailBinding
import com.glopezsanchez.rickmortytest.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterDetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailBinding
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationIcon(R.drawable.arrow_left)
        binding.toolbar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailUiState.collectLatest { state ->
                state.character?.apply {
                    binding.image.load(this.picture) {
                        transformations(
                            CircleCropTransformation()
                        )
                    }
                    binding.toolbar.title = this.name
                }

                state.content?.apply {
                    binding.infoText.text =
                        Html.fromHtml(state.content, Html.FROM_HTML_MODE_LEGACY)
                }

                binding.loadingContainer.isVisible = state.isLoading

                if(state.errorMessage != null) {
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}