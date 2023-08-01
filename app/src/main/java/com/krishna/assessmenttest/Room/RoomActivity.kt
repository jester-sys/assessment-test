package com.krishna.assessmenttest.Room


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krishna.assessmenttest.databinding.ActivityRoomBinding


class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        val recyclerViewAdapter = RecyclerViewAdapter(this)
        binding.RV.layoutManager = LinearLayoutManager(this)
        binding.RV.adapter = recyclerViewAdapter

        // Use the Kotlin lambda syntax with the 'observe' extension function
        myViewModel.getAllData().observe(this) { newData ->
            recyclerViewAdapter.setData(newData)
        }
    }
}