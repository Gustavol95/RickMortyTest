package com.glopezsanchez.rickmortytest.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.glopezsanchez.rickmortytest.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersHolder

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel> { ParametersHolder() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}