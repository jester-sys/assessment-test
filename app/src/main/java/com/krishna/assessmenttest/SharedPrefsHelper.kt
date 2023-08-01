package com.krishna.assessmenttest

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

//class SharedPrefsHelper(private val context: Context) {
//
//    companion object {
//        private const val PREFS_NAME = "UserDataPrefs"
//        private const val KEY_NAME = "name"
//        private const val KEY_PHONE_NUMBER = "phoneNumber"
//        private const val KEY_EMAIL = "email"
//        private const val KEY_PROFILE_PIC = "profilePic" // Updated key name for the image
//        private const val KEY_DOB = "dob"
//    }
//
//    private val prefs: SharedPreferences =
//        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//    fun saveUserData(name: String, phoneNumber: String, email: String, profilePicUri: Uri?, dob: String) {
//        val editor = prefs.edit()
//        editor.putString(KEY_NAME, name)
//        editor.putString(KEY_PHONE_NUMBER, phoneNumber)
//        editor.putString(KEY_EMAIL, email)
//        editor.putString(KEY_DOB, dob)
//
//        // Convert the image Uri to Base64 string before saving, only if it's not null
//        profilePicUri?.let { uri ->
//            val imageString = convertImageToBase64(uri)
//            editor.putString(KEY_PROFILE_PIC, imageString)
//        } ?: editor.remove(KEY_PROFILE_PIC) // Remove the key if image is null
//
//        editor.apply()
//    }
//
//    fun getUserData(): SharedUserData {
//        val name = prefs.getString(KEY_NAME, "") ?: ""
//        val phoneNumber = prefs.getString(KEY_PHONE_NUMBER, "") ?: ""
//        val email = prefs.getString(KEY_EMAIL, "") ?: ""
//        val dob = prefs.getString(KEY_DOB, "") ?: ""
//        val imageString = prefs.getString(KEY_PROFILE_PIC, "")
//
//        // Convert the Base64 encoded string back to Uri, if it's not null or empty
//        val profilePicUri = if (!imageString.isNullOrEmpty()) convertBase64ToImage(imageString) else null
//
//        return SharedUserData(name, phoneNumber, email, dob, profilePicUri)
//    }
//
//    private fun convertImageToBase64(imageUri: Uri): String {
//        val inputStream = context.contentResolver.openInputStream(imageUri)
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        inputStream?.use {
//            val buffer = ByteArray(1024)
//            var length: Int
//            while (it.read(buffer).also { length = it } != -1) {
//                byteArrayOutputStream.write(buffer, 0, length)
//            }
//        }
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }
//
//    private fun convertBase64ToImage(imageString: String?): Uri? {
//        if (imageString.isNullOrEmpty()) {
//            return null
//        }
//
//        val decodedBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
//        val imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//
//        // Save the decoded bitmap to a file
//        val fileName = "profile_image.jpg"
//        val outputStream: FileOutputStream
//        try {
//            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//
//        return Uri.fromFile(File(context.filesDir, fileName))
//    }
//
//}



class SharedPrefsHelper(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "UserDataPrefs"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_EMAIL = "email"
        private const val KEY_PROFILE_PIC = "profilePic"
        private const val KEY_DOB = "dob"
        private const val KEY_USER_COUNT = "userCount" // New key to store the count of users
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserData(userData: SharedUserData) {
        val userCount = prefs.getInt(KEY_USER_COUNT, 0) + 1
        val editor = prefs.edit()
        editor.putString(KEY_NAME + userCount, userData.name)
        editor.putString(KEY_PHONE_NUMBER + userCount, userData.phoneNumber)
        editor.putString(KEY_EMAIL + userCount, userData.email)
        editor.putString(KEY_DOB + userCount, userData.dob)

        // Convert the image Uri to Base64 string before saving, only if it's not null
        userData.photoProfile?.let { uri ->
            val imageString = convertImageToBase64(uri)
            editor.putString(KEY_PROFILE_PIC + userCount, imageString)
        }

        editor.putInt(KEY_USER_COUNT, userCount)
        editor.apply()
    }

    fun getAllUserData(): List<SharedUserData> {
        val userCount = prefs.getInt(KEY_USER_COUNT, 0)
        val userDataList = mutableListOf<SharedUserData>()

        for (i in 1..userCount) {
            val name = prefs.getString(KEY_NAME + i, "") ?: ""
            val phoneNumber = prefs.getString(KEY_PHONE_NUMBER + i, "") ?: ""
            val email = prefs.getString(KEY_EMAIL + i, "") ?: ""
            val dob = prefs.getString(KEY_DOB + i, "") ?: ""
            val imageString = prefs.getString(KEY_PROFILE_PIC + i, "")

            // Convert the Base64 encoded string back to Uri, if it's not null or empty
            val profilePicUri = if (!imageString.isNullOrEmpty()) convertBase64ToImage(imageString) else null

            val userData = SharedUserData(name, phoneNumber, email, dob, profilePicUri)
            userDataList.add(userData)
        }

        return userDataList
    }

    private fun convertImageToBase64(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        inputStream?.use {
            val buffer = ByteArray(1024)
            var length: Int
            while (it.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
        }
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun convertBase64ToImage(imageString: String?): Uri? {
        if (imageString.isNullOrEmpty()) {
            return null
        }

        val decodedBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        val imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        // Save the decoded bitmap to a file
        val fileName = "profile_image_$timestamp.jpg"
        val outputStream: FileOutputStream
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return Uri.fromFile(File(context.filesDir, fileName))
    }

    private val timestamp: String
        get() = System.currentTimeMillis().toString()

}