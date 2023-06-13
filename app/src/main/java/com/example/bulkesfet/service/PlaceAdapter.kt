package com.example.bulkesfet.service

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.OneplacelayoutBinding
import com.example.bulkesfet.model.PlaceModel
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable
import com.example.bulkesfet.view.app.SearchFragmentArgs
import com.example.bulkesfet.view.app.SearchFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/*
Adapter çalıştığında buraya placeList gelmekte. İçeriği placeList dizisidir.
İçeride ilk çalışan kısım getItemCount ve OnBindViewHolder çalışır.
Onbind içerisinden sırasıyla listenin içeriğini gezdirme işlemi başlıyor.
Her işlemde onCreateViewHolder düzeni sağlıyor.
 */
class PlaceAdapter(private val placeList: ArrayList<PlaceModel>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    val firebaseAuth=FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance().reference

    inner class PlaceViewHolder(private val binding: OneplacelayoutBinding, var view: View) :
        RecyclerView.ViewHolder(binding.root) {
        val placeConstraint = binding.mainConstraint
        private val placeName = binding.placeNameText
        private val placeImage = binding.placeImg
        private val placeDescription= binding.placeDescription
        private val favBtn = binding.favCheck
        private val favLogin = binding.loginBTN


        fun setData(placeDetail: PlaceModel) {
            placeName.text = placeDetail.placeName
            placeDescription.text=setText100(placeDetail.description)
            placeImage.getImage(placeDetail.images[0], progressDrawable(view.context))
            if (firebaseAuth.currentUser!=null){
                favBtn.visibility=View.VISIBLE
                favLogin.visibility=View.GONE
                //TODO Favoriye alındığında burada işlem yaptır. Aşağıda örnek kod var
                favBtn.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                        favBtn.setButtonDrawable(R.drawable.ic_removebookmark)
                    else
                        favBtn.setButtonDrawable(R.drawable.ic_addbookmark)

                }
            }else{
                binding.loginBTN.setOnClickListener {
                    val alert= AlertDialog.Builder(view.context)
                    alert.setTitle(R.string.needLogin)
                        .setIcon(R.drawable.ic_person_add)
                        .setNeutralButton(R.string.ok) { dialog, _ ->
                            dialog.cancel()
                        }
                        .create().show()
                }
                favBtn.visibility=View.GONE
                favLogin.visibility=View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding =
            OneplacelayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewCreate =
            LayoutInflater.from(parent.context).inflate(R.layout.oneplacelayout, parent, false)
        return PlaceViewHolder(binding, viewCreate)

    }


    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val createPlaceNow =
            placeList[position]
        holder.setData(createPlaceNow)
        holder.placeConstraint.setOnClickListener {
            val mId=createPlaceNow.id.toInt()
            val action=SearchFragmentDirections.actionSearchFragmentToOnePlaceFragment(mId)
            Navigation.findNavController(it).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaceList(newPlaceList: List<PlaceModel>) {
        placeList.clear()
        placeList.addAll(newPlaceList)
        notifyDataSetChanged()
    }

    fun setText100(longText: String): String {
        val maxLength = 100

        val trimmedText = if (longText.length > maxLength) {
            val endIndex = longText.indexOf('.', maxLength)
            if (endIndex != -1) {
                longText.substring(0, endIndex + 1)
            } else {
                longText.substring(0, maxLength)
            }
        } else {
            longText
        }
        return trimmedText
    }

}


/*



inner class FoodViewHolder(var view:View): RecyclerView.ViewHolder(view) {
    var foodName=view.foodNameText
    var foodImage = view.imgFood
    var favBtn = view.favCheck
    fun setData(createFoodLineNow: MealData.Results, position: Int) {
        checkFav(createFoodLineNow)
        foodName.text= createFoodLineNow.resultName
        foodImage.getImage(createFoodLineNow.image!!, progressDrawable(view.context))

        favBtn.setOnClickListener {
            if (favBtn.isChecked) {
                val itemId=createFoodLineNow.mId.toString()
                val itemName=createFoodLineNow.resultName.toString()
                val itemImage=createFoodLineNow.image.toString()
                val itemContent=createFoodLineNow.content.toString()
                val newFavoriteItem = FavoritesItem(itemId,itemName,itemImage,itemContent)
                database
                    .child("Users")
                    .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                    .child("Favorites")
                    .child(foodName.text.toString())
                    .setValue(newFavoriteItem)

            } else {
                database
                    .child("Users")
                    .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                    .child("Favorites")
                    .child(foodName.text.toString())
                    .removeValue()
            }

        }
    }

    private fun checkFav(createFoodLineNow: MealData.Results) {
        favBtn.isChecked=false
        val query = database.child("Users")
            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
            .child("Favorites")
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.child("itemId").value == createFoodLineNow.mId.toString()) {
                        favBtn.isChecked = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}



 */
