package com.krishna.assessmenttest

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.krishna.assessmenttest.Firebase.FirebaseActivity
import com.krishna.assessmenttest.Firebase.FirebaseHelper
import com.krishna.assessmenttest.Room.DataBase
import com.krishna.assessmenttest.Room.Model
import com.krishna.assessmenttest.Room.RoomActivity
import com.krishna.assessmenttest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var profilePhotoUri: Uri? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var localDatabase: DataBase
    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        localDatabase = DataBase.getInstance(this)

        binding.btnSave.setOnClickListener {
            saveUserData()
        }

        binding.etDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSharedPreferences.setOnClickListener {
            startActivity(Intent(this, SharedPreferencesActivity::class.java))
        }
        binding.Roomsave.setOnClickListener {
            startActivity(Intent(this, RoomActivity::class.java))
        }

        binding.btnFirebase.setOnClickListener {
            startActivity(Intent(this, FirebaseActivity::class.java))
        }

        binding.profilePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 10)
        }
    }

    private fun saveUserData() {
        val name = binding.etName.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val email = binding.etEmail.text.toString()
        val dob = binding.etDateOfBirth.text.toString()

        val user = Model(
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            dob = dob,
            photoProfile = profilePhotoUri
        )

        CoroutineScope(Dispatchers.IO).launch {
            localDatabase.dataDao().insertData(user)
        }


        // Save data in SharedPreferences
        val sharedPrefsHelper = SharedPrefsHelper(this)
        sharedPrefsHelper.saveUserData(SharedUserData(name, phoneNumber, email, dob, profilePhotoUri))


        // Save data in Firebase
        val firebaseHelper = FirebaseHelper(this)
        firebaseHelper.saveUserData(
            name,
            phoneNumber,
            email,
            dob,
            profilePhotoUri?.toString() ?: "" // Provide a default empty string when profilePhotoUri is null
        )

        clearFields()
    }

    private fun clearFields() {
        binding.etName.text.clear()
        binding.etPhoneNumber.text.clear()
        binding.etEmail.text.clear()
        binding.etDateOfBirth.text.clear()
        binding.profilePhoto.setImageResource(R.drawable.ic_placeholder) // Set a placeholder image
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                // Handle date selection here
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                binding.etDateOfBirth.setText(selectedDate)
            },
            year,
            month,
            day
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == Activity.RESULT_OK && data?.data != null) {
            profilePhotoUri = data.data // Store the selected image URI
            binding.profilePhoto.setImageURI(profilePhotoUri) // Display the selected image in ImageView
        }
    }

}