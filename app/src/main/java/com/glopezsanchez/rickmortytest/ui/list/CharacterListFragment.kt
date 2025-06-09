package com.glopezsanchez.rickmortytest.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.glopezsanchez.rickmortytest.databinding.FragmentCharacterListBinding
import com.glopezsanchez.rickmortytest.ui.MainViewModel

class CharacterListFragment : Fragment() {

    private lateinit var binding: FragmentCharacterListBinding
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }
}