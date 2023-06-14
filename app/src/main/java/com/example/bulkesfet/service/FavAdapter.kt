package com.example.bulkesfet.service

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.OneplacelayoutBinding
import com.example.bulkesfet.model.PlaceModel
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable
import com.example.bulkesfet.view.app.FavoritesFragment
import com.example.bulkesfet.view.app.FavoritesFragmentDirections
import com.example.bulkesfet.view.app.SearchFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavAdapter(mFragment: Fragment, private val favList: ArrayList<PlaceModel>) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    val myFragment=mFragment
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    inner class FavViewHolder(private val binding: OneplacelayoutBinding, var view: View) : RecyclerView.ViewHolder(binding.root) {

        val placeConstraint = binding.mainConstraint
        private val placeName = binding.placeNameText
        private val placeImage = binding.placeImg
        private val placeDescription= binding.placeDescription
        private val favBtn = binding.favCheck

        fun setData(placeDetail: PlaceModel, position: Int) {
            placeName.text = placeDetail.placeName
            placeDescription.text=setText100(placeDetail.description)
            placeImage.getImage(placeDetail.images[0], progressDrawable(view.context))

            favBtn.isChecked=true
            favBtn.setButtonDrawable(R.drawable.ic_removebookmark)
            favBtn.setOnClickListener {
                favList.removeAt(position)
                firebaseDatabase
                    .child("Users")
                    .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                    .child("Favorites")
                    .child(placeDetail.id)
                    .removeValue()
                (myFragment as FavoritesFragment).recyclerOlustur(favList)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.FavViewHolder {
        val binding =
            OneplacelayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewCreate =
            LayoutInflater.from(parent.context).inflate(R.layout.oneplacelayout, parent, false)
        return FavViewHolder(binding, viewCreate)
    }

    override fun onBindViewHolder(holder: FavAdapter.FavViewHolder, position: Int) {
        val createPlaceNow = favList[position]
        holder.setData(createPlaceNow,position)
        holder.placeConstraint.setOnClickListener {
            val mId=createPlaceNow.id.toInt()
            val action= FavoritesFragmentDirections.actionFavoritesFragmentToOnePlaceFragment(mId)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavList(newFavList: ArrayList<PlaceModel>){
        favList.clear()
        favList.addAll(newFavList)
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