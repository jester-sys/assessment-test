package com.krishna.assessmenttest.Firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.krishna.assessmenttest.databinding.ActivityFirebaseBinding

class FirebaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirebaseBinding
    private lateinit var adapter: UserDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter = UserDataAdapter(this)
        binding.rvUserData.layoutManager = LinearLayoutManager(this)
        binding.rvUserData.adapter = adapter

        val firebaseHelper = FirebaseHelper(this)
        firebaseHelper.getUserDataList(object : FirebaseHelper.OnUserDataListener {
            override fun onDataLoaded(userDataList: List<UserData>) {
                adapter.setData(userDataList)
            }
        })
    }
}
