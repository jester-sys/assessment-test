package com.krishna.assessmenttest.Firebase


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.krishna.assessmenttest.R
import com.krishna.assessmenttest.databinding.ItemUserDataBinding
import com.squareup.picasso.Picasso

class UserDataAdapter(private val context: Context) : RecyclerView.Adapter<UserDataAdapter.UserDataViewHolder>() {

    private val userDataList = mutableListOf<UserData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<UserData>) {
        userDataList.clear()
        userDataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserDataBinding.inflate(inflater, parent, false)
        return UserDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        val userData = userDataList[position]
        holder.bind(userData, position) // Pass the position to the ViewHolder
    }

    fun updateUserData(position: Int, updatedUserData: UserData) {
        if (position in 0 until userDataList.size) {
            userDataList[position] = updatedUserData
            notifyItemChanged(position)
        }
    }

    fun deleteUserData(position: Int) {
        if (position in 0 until userDataList.size) {
            val userData = userDataList[position]
            val userId = userData.userId // Assuming you have a property in the UserData class for the unique user ID
            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserData")
            databaseReference.child(userId!!).removeValue()
            userDataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class UserDataViewHolder(private val binding: ItemUserDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData, position: Int) {
            binding.tvName.text = userData.name
            binding.tvPhoneNumber.text = userData.phoneNumber
            binding.tvEmail.text = userData.email
            binding.tvDateOfBirth.text = userData.dob


            if (userData.photoProfile!!.isNotEmpty()) {
        Picasso.get().load(Uri.parse(userData.photoProfile)).into(binding.ivProfilePic)
    } else {
        // Optionally, you can set a placeholder image or default image if there is no profile picture.
        Picasso.get().load(R.drawable.ic_placeholder).into(binding.ivProfilePic)
    }
            binding.btnDelete.setOnClickListener {
                showDeleteDialog(position)
            }

            binding.btnEdit.setOnClickListener {
                showEditDialog(userData, position) // Pass the position to the dialog
            }
        }


        private fun showEditDialog(userData: UserData, position: Int) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_dialog_layout, null)
            val editedNameEditText = dialogView.findViewById<EditText>(R.id.etName)
            val editedPhoneNumberEditText = dialogView.findViewById<EditText>(R.id.etPhoneNumber)
            val editedEmailEditText = dialogView.findViewById<EditText>(R.id.etEmail)

            // Pre-fill the dialog fields with the existing user data
            editedNameEditText.setText(userData.name)
            editedPhoneNumberEditText.setText(userData.phoneNumber)
            editedEmailEditText.setText(userData.email)

            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Edit User Data")
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, _ ->
                    // Save the edited data here
                    val editedName = editedNameEditText.text.toString()
                    val editedPhoneNumber = editedPhoneNumberEditText.text.toString()
                    val editedEmail = editedEmailEditText.text.toString()

                    // Create a new UserData object with the edited data
                    val editedUserData = UserData(
                        userId = userData.userId,
                        name = editedName,
                        phoneNumber = editedPhoneNumber,
                        email = editedEmail,
                        dob = userData.dob,
                        photoProfile = userData.photoProfile
                    )

                    // Update the data in the adapter and notify the RecyclerView
                    updateUserData(position, editedUserData)

                    // Update the data on Firebase
                    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserData")
                    databaseReference.child(userData.userId!!).setValue(editedUserData) // Use userId as the key for the update

                    Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            // Show the dialog
            alertDialog.show()
        }
    }

    private fun showDeleteDialog(position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog_layout, null)

        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete User Data")
            .setView(dialogView)
            .setPositiveButton("Delete") { dialog, _ ->
                // Call the function to delete the user data
                deleteUserData(position)

                Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Show the dialog
        alertDialog.show()
    }
}

