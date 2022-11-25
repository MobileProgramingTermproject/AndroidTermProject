package com.example.watch

import android.app.Activity
import android.os.Bundle
import com.example.watch.databinding.ActivityWatchMainBinding

class Watch_MainActivity : Activity() {

    private lateinit var binding: ActivityWatchMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWatchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}