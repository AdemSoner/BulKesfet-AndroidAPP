package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel:ViewModel() {
    val loading= MutableLiveData<Boolean>()
    val error= MutableLiveData<String>()
    val userLoggedIn = MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance()
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference
    private val firebaseStorage=FirebaseStorage.getInstance().reference

    fun userLogDetail(){
        userLoggedIn.value = firebaseAuth.currentUser!=null
    }

    fun logout(){
        firebaseAuth.signOut()
        userLogDetail()
    }

    fun setViewComponents():User{
        return User(firebaseAuth.currentUser?.email!!,"")
    }
}