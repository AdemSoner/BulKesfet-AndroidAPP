package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.Comments
import com.example.bulkesfet.model.MyDate
import com.example.bulkesfet.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import kotlin.random.Random

class ProfileViewModel:ViewModel() {
    val loading= MutableLiveData<Boolean>()
    val loadUserConstraint=MutableLiveData<Boolean>()
    val error= MutableLiveData<String>()
    val userLoggedIn = MutableLiveData<Boolean>()

    val comment = MutableLiveData<Comments>()
    val user = MutableLiveData<UserProfile>()
    val commentCount = MutableLiveData<Int>()
    private val list = mutableListOf<DataSnapshot>()


    private val firebaseAuth=FirebaseAuth.getInstance()
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference
    private val firebaseStorage=FirebaseStorage.getInstance().reference

    fun userLogDetail(){
        loading.value=true
        userLoggedIn.value = firebaseAuth.currentUser!=null
    }

    fun logout(){
        firebaseAuth.signOut()
        userLogDetail()
    }

    fun getViewComponents(){
        getCount()
        getUser()
    }

    fun getUserUID():String{
        return firebaseAuth.uid.toString()
    }

    private fun getCount() {
        var countDown=0
        firebaseDatabase.child("Comments").get().addOnSuccessListener {
                for(snapshot in it.children){
                    if (snapshot.value!=null){
                        if(snapshot.child("userUID").value==firebaseAuth.currentUser!!.uid){
                            list.add(snapshot)
                            countDown++
                        }
                    }
                }
                if (countDown!=0){
                    val randomComment=list[Random.nextInt(countDown)]
                    val placeID=randomComment.child("placeID").value.toString()
                    val placeName=randomComment.child("placeName").value.toString()
                    val placeImage=randomComment.child("placeImage").value.toString()
                    val placeRate=randomComment.child("rate").value.toString().toInt()
                    val placeComment=randomComment.child("comment").value.toString()
                    val userUID=randomComment.child("userUID").value.toString()
                    val userName=randomComment.child("userName").value.toString()
                    val userImage=randomComment.child("userImage").value.toString()
                    val placeDay=randomComment.child("date").child("day").value.toString()
                    val placeMonth=randomComment.child("date").child("month").value.toString()
                    val placeYear=randomComment.child("date").child("year").value.toString()
                    val commentDate=MyDate(placeDay,placeMonth,placeYear)
                    val commentOne= Comments(placeID,placeName,placeImage,userUID,userName,userImage,placeRate,placeComment,commentDate)
                    comment.value=commentOne
                    commentCount.value=countDown
                }
            }
    }

    private fun getUser(){
        firebaseDatabase.child("Users").child(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                if (it.value!=null){
                    val userNameSurname=it.child("nameSurname").value.toString()
                    val userEmail=it.child("email").value.toString()
                    val imageURL=it.child("imageURL").value.toString()
                    user.value=UserProfile(userNameSurname,userEmail,imageURL)
                    loadUserConstraint.value=true
                }
            }

    }

}