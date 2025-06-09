package com.glopezsanchez.rickmortytest.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.glopezsanchez.rickmortytest.databinding.FragmentCharacterDetailBinding
import com.glopezsanchez.rickmortytest.ui.MainViewModel

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

    }
}