package com.krishna.assessmenttest.Firebase

import android.net.Uri

data class UserData(
    val userId: String? = "",
    val name: String? = "",
    val phoneNumber: String = "",
    val email: String = "",
    val dob: String = "",
    val photoProfile: String ="" // New field for profile photo URL

)

