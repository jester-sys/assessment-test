package com.krishna.assessmenttest

import android.net.Uri

data class SharedUserData (
     val name: String,
     val phoneNumber: String,
     val email: String,
     val dob: String,
     val photoProfile: Uri?
         )
