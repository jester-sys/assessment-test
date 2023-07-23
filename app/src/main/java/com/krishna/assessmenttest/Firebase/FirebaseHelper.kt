package com.krishna.assessmenttest.Firebase


import android.content.Context
import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirebaseHelper(context: Context) {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("UserData")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    fun saveUserData(
        name: String,
        phoneNumber: String,
        email: String,
        dob: String,
        photoProfileUri: String
    ) {
        val userId = databaseReference.push().key ?: return

        // Upload the profile photo to Firebase Storage and get the download URL
        uploadProfilePhoto(userId, photoProfileUri) { downloadUrl ->
            val userData = UserData(userId, name, phoneNumber, email, dob, downloadUrl.toString())
            databaseReference.child(userId).setValue(userData)
        }
    }

    private fun uploadProfilePhoto(userId: String, imageUri: String, onComplete: (Uri) -> Unit) {
        if (imageUri.isEmpty()) {
            onComplete(Uri.EMPTY) // Return an empty Uri if the imageUri is empty
            return
        }

        val imageRef = storageReference
            .child("profile_images")
            .child(userId)

        val uploadTask = imageRef.putFile(Uri.parse(imageUri)) // Convert the String Uri to Uri object

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Get the download URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onComplete(downloadUri)
            }
        }.addOnFailureListener { exception ->
            // Handle upload failure
            onComplete(Uri.EMPTY) // Return an empty Uri if the upload fails
        }
    }

    fun getUserDataList(listener: OnUserDataListener) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userDataList = mutableListOf<UserData>()
                for (dataSnapshot in snapshot.children) {
                    val userData = dataSnapshot.getValue(UserData::class.java)
                    userData?.let {
                        userDataList.add(it)
                    }
                }
                listener.onDataLoaded(userDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if necessary
            }
        })
    }

    interface OnUserDataListener {
        fun onDataLoaded(userDataList: List<UserData>)
    }
}



//class FirebaseHelper(private val context: Context) {
//
//    private val databaseReference: DatabaseReference =
//        FirebaseDatabase.getInstance().getReference("UserData")
//    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
//
//    fun saveUserData(
//        name: String,
//        phoneNumber: String,
//        email: String,
//        dob: String,
//        photoProfileUri: String
//    ) {
//        val userId = databaseReference.push().key ?: return
//
//        // Upload the profile photo to Firebase Storage and get the download URL
//        uploadProfilePhoto(userId, photoProfileUri) { downloadUrl ->
//            val userData = UserData(userId, name, phoneNumber, email, dob, downloadUrl.toString())
//            databaseReference.child(userId).setValue(userData)
//        }
//    }
//
//    private fun uploadProfilePhoto(userId: String, imageUri: String, onComplete: (Uri) -> Unit) {
//        if (imageUri.isEmpty()) {
//            onComplete(Uri.EMPTY) // Return an empty Uri if the imageUri is empty
//            return
//        }
//
//        val imageRef = storageReference
//            .child("profile_images")
//            .child(userId)
//
//        // Convert the imageUri to a File to be used in putFile()
//        val imageFile = File(imageUri)
//
//        val uploadTask = imageRef.putFile(Uri.fromFile(imageFile))
//
//        uploadTask.addOnSuccessListener { taskSnapshot ->
//            // Get the download URL of the uploaded image
//            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                onComplete(downloadUri)
//            }
//        }.addOnFailureListener { exception ->
//            // Handle upload failure
//            onComplete(Uri.EMPTY) // Return an empty Uri if the upload fails
//        }
//    }
//
//    fun getUserDataList(listener: OnUserDataListener) {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val userDataList = mutableListOf<UserData>()
//                for (dataSnapshot in snapshot.children) {
//                    val userData = dataSnapshot.getValue(UserData::class.java)
//                    userData?.let {
//                        userDataList.add(it)
//                    }
//                }
//                listener.onDataLoaded(userDataList)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle the error if necessary
//            }
//        })
//    }
//
//    interface OnUserDataListener {
//        fun onDataLoaded(userDataList: List<UserData>)
//    }
//}
