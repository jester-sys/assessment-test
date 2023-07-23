package com.krishna.assessmenttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.krishna.assessmenttest.databinding.ActivitySharedPreferencesBinding

class SharedPreferencesActivity : AppCompatActivity() {
    private lateinit var adapter: SharedUserDataAdapter
    private lateinit var binding: ActivitySharedPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and its adapter
        adapter = SharedUserDataAdapter(this)
        binding.rvUserData.layoutManager = LinearLayoutManager(this)
        binding.rvUserData.adapter = adapter

        // Retrieve the list of UserData from SharedPrefsHelper and update the adapter
        val sharedPrefsHelper = SharedPrefsHelper(this)
        val userData = sharedPrefsHelper.getUserData()

        // Make sure to pass the 'photoProfile' parameter when creating the UserData object
        val userDataList = listOf(userData)
        adapter.setData(userDataList)
    }
}
