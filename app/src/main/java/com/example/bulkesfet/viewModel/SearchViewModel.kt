package com.example.bulkesfet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkesfet.model.PlaceModel
import com.example.bulkesfet.service.PlaceAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchViewModel : ViewModel() {
    var placeList = MutableLiveData<List<PlaceModel>>()
    var placeError = MutableLiveData<Boolean>()
    var placeLoading = MutableLiveData<Boolean>()

    private val firebaseAuth = FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    val placeArrayList = ArrayList<PlaceModel>()


    fun refreshDataFromFirebase(queryPlaceName:String){
        placeError.value = false
        placeLoading.value = true
        val query = FirebaseDatabase.getInstance().reference.child("Places").orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        val placeID = singleSnapshot.child("id").value.toString()
                        val placeName = singleSnapshot.child("placeName").value.toString()
                        val placeCategory = singleSnapshot.child("category").value.toString()
                        val placeDescription = singleSnapshot.child("description").value.toString()
                        val placeAdress = singleSnapshot.child("address").value.toString()
                        val placePrice = singleSnapshot.child("price").value.toString()
                        val placeImagesCount = singleSnapshot.child("images").childrenCount
                        val placeImages= arrayListOf<String>()
                        for (a in 0 until placeImagesCount){
                            placeImages.add(singleSnapshot.child("images").child(a.toString()).value.toString())
                        }
                        val places = PlaceModel(placeID,placeName,placeCategory,placeDescription,placePrice,placeAdress,placeImages)
                        when (queryPlaceName){
                            "historical" -> {
                                if (places.category==queryPlaceName)
                                    placeArrayList.add(places)

                            }
                            "beach" -> {
                                if (places.category==queryPlaceName)
                                    placeArrayList.add(places)

                            }
                            "natural" -> {
                                if (places.category==queryPlaceName)
                                    placeArrayList.add(places)

                            }
                            "museum" -> {
                                if (places.category==queryPlaceName)
                                    placeArrayList.add(places)

                            }
                            else -> placeArrayList.add(places)

                        }
                    }
                }
                placeList.value=placeArrayList
                placeLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                placeError.value=true
            }
        })
    }

}