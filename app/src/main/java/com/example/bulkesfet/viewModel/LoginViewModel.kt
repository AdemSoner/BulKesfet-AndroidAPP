package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel() {
    val loginErrorMessage = MutableLiveData<String>()
    val loginInProgress = MutableLiveData<Boolean>()
    val loginIsSuccess = MutableLiveData<Boolean>()

    fun signInWithFirebase(userInformation:User){
        loginInProgress.value=true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            userInformation.eposta,
            userInformation.password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loginIsSuccess.value=true
                }else{
                    loginIsSuccess.value=false
                    loginErrorMessage.value=it.exception?.message.toString()
                }
                loginInProgress.value=false
            }
    }
}