package com.krishna.assessmenttest.Room

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Model(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val dob: String,
     val photoProfile: Uri ?
)