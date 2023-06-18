package com.example.bulkesfet.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.FragmentPlaceCommentsBinding
import com.example.bulkesfet.databinding.OnecommentlayoutBinding
import com.example.bulkesfet.model.Comments
import com.example.bulkesfet.model.NewCommentModel
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable
import com.example.bulkesfet.view.app.NewCommentFragment
import com.google.firebase.auth.FirebaseAuth

class PlaceCommentAdapter(private val commentList: ArrayList<Comments>) :
    RecyclerView.Adapter<PlaceCommentAdapter.PlaceCommentViewHolder>() {

    inner class PlaceCommentViewHolder(private val binding: OnecommentlayoutBinding,private val mainBinding: FragmentPlaceCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val userName = binding.userNameText
        private val userImage = binding.Img
        private val userRate = binding.userRateText
        private val userDate = binding.commentDateText
        private val userComment = binding.userComment
        private val firebaseAuth=FirebaseAuth.getInstance().currentUser

        fun setData(commentDetail: Comments){
            checkUser(commentDetail)
            userName.text=commentDetail.userName
            userImage.getImage(commentDetail.userImage, progressDrawable(binding.root.context))
            userRate.text=commentDetail.rate.toString()
            if (commentDetail.rate==5)
                userRate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_starfull, 0, 0, 0)
            else
                userRate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_starhalf, 0, 0, 0)
            userComment.text=commentDetail.comment
            val date=commentDetail.date.day+"/"+commentDetail.date.month+"/"+commentDetail.date.year
            userDate.text=date
        }

        private fun checkUser(comment:Comments) {
            if (firebaseAuth!=null) {
                if (firebaseAuth.uid==comment.userUID){
                    binding.userImagesGroup.visibility=View.VISIBLE
                } else {
                    binding.userImagesGroup.visibility=View.GONE
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceCommentViewHolder {
        val binding =
            OnecommentlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mainBinding= FragmentPlaceCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceCommentViewHolder(binding,mainBinding)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: PlaceCommentViewHolder, position: Int) {
        val createCommentNow = commentList[position]
        holder.setData(createCommentNow)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaceList(newCommentList: List<Comments>) {
        commentList.clear()
        notifyDataSetChanged()
        commentList.addAll(newCommentList)
    }


}

