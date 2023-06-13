package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.PlaceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OnePlaceViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val placeDetails = MutableLiveData<PlaceModel>()
    val error = MutableLiveData<String>()

    fun getDatas(mID: Int) {
        loading.value = true
        val placeID = mID.toString()
        val query = FirebaseDatabase.getInstance().reference.child("Places").child(placeID)
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val placeName = snapshot.child("placeName").value.toString()
                val placeCategory = snapshot.child("category").value.toString()
                val placePrice = snapshot.child("price").value.toString()
                val placeAddress = snapshot.child("address").value.toString()
                val placeDescription = snapshot.child("description").value.toString()
                val placeImagesCount = snapshot.child("images").childrenCount
                val placeImages = arrayListOf<String>()
                for (a in 0 until placeImagesCount) {
                    placeImages.add(
                        snapshot.child("images").child(a.toString()).value.toString()
                    )
                }
                val place = PlaceModel(
                    mID.toString(),
                    placeName,
                    placeCategory,
                    placeDescription,
                    placePrice,
                    placeAddress,
                    placeImages
                )
                placeDetails.value = place
                loading.value = false
            }


            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })


    }

    fun getUser():Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
}