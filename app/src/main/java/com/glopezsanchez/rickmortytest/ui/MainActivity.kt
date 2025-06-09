package com.glopezsanchez.rickmortytest.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.glopezsanchez.rickmortytest.R
import com.glopezsanchez.rickmortytest.databinding.ActivityMainBinding
import com.glopezsanchez.rickmortytest.ui.detail.CharacterDetailFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersHolder

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel> { ParametersHolder() }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            viewModel.navigationEvents.collectLatest {
                processNavigationEvent(it)
            }
        }
    }

    private fun processNavigationEvent(mainNavState: MainNavState) {
        if (mainNavState.navigationEvent == null) return

        when (mainNavState.navigationEvent) {
            NavigationEvent.GoToDetails -> supportFragmentManager
                .beginTransaction()
                .replace(binding.container.id, CharacterDetailFragment(), "tag")
                .addToBackStack("tag")
                .commit()

            NavigationEvent.GoBack -> supportFragmentManager.popBackStack()
        }

        viewModel.navEventProcessed()
    }
}