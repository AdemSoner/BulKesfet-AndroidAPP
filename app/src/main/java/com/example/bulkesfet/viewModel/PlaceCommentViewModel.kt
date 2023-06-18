package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.Comments
import com.example.bulkesfet.model.MyDate
import com.example.bulkesfet.model.NewCommentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlaceCommentViewModel:ViewModel() {
    val loading= MutableLiveData<Boolean>()
    val commentError = MutableLiveData<Boolean>()
    val commentList = MutableLiveData<List<Comments>>()
    val userImage=MutableLiveData<String>()
    val userName=MutableLiveData<String>()


    val userDetail=MutableLiveData<NewCommentModel>()
    val commentChecker=MutableLiveData(0)
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    val commentArrayList = ArrayList<Comments>()
    val commentCount=MutableLiveData<Int>()


    fun getCommentsFromFirebase(placeID:String){
        loading.value=true
        var itemCountNow=0
        val query = firebaseDatabase.child("Comments")
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null)
                        if (singleSnapshot.child("placeID").value==placeID){
                            val placeName= singleSnapshot.child("placeName").value.toString()
                            val placeImage= singleSnapshot.child("placeImage").value.toString()
                            val userUID=singleSnapshot.child("userUID").value.toString()
                            userName.value = singleSnapshot.child("userName").value.toString()
                            val userRate = singleSnapshot.child("rate").value.toString()
                            userImage.value = singleSnapshot.child("userImage").value.toString()
                            val userComment = singleSnapshot.child("comment").value.toString()
                            val day=singleSnapshot.child("date").child("day").value.toString()
                            val month=singleSnapshot.child("date").child("month").value.toString()
                            val year=singleSnapshot.child("date").child("year").value.toString()
                            val date=MyDate(day,month,year)
                            val comment = Comments(placeID,placeName,placeImage,userUID,userName.value.toString(),userImage.value.toString(),userRate.toInt(),userComment,date)
                            commentArrayList.add(comment)
                            itemCountNow++
                        }
                    val userUID=singleSnapshot.child("userUID").value.toString()
                    if (userUID==FirebaseAuth.getInstance().currentUser!!.uid)
                        commentChecker.value=1
                }
                if (itemCountNow!=0){
                    commentList.value = commentArrayList
                }
                commentCount.value=itemCountNow
                loading.value=false
            }
            override fun onCancelled(error: DatabaseError) {
                commentError.value=true
            }
        })
    }

    fun getUserDetails(){
        firebaseDatabase.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener {
                userImage.value=it.child("imageURL").value.toString()
                userName.value=it.child("nameSurname").value.toString()
            }
    }

    fun getUser():Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }


}