package com.example.bulkesfet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.OneplacelayoutBinding
import com.example.bulkesfet.databinding.OneusercommentlayoutBinding
import com.example.bulkesfet.model.Comments
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable

class UserCommentAdapter (private val commentList: ArrayList<Comments>) :
    RecyclerView.Adapter<UserCommentAdapter.UserCommentViewHolder>(){
    inner class UserCommentViewHolder(private val binding: OneusercommentlayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val placeName= binding.placeNameText
        private val placeImage= binding.placeImg
        private val userRate=binding.placeRateText
        private val comment= binding.placeDescription
        private val date= binding.commentDateText
        fun setData(commentDetail:Comments){
            placeName.text=commentDetail.placeName
            placeImage.getImage(commentDetail.placeImage, progressDrawable(binding.root.context))
            userRate.text=commentDetail.rate.toString()
            setStars(commentDetail.rate)
            comment.text=commentDetail.comment
            val string="${commentDetail.date.day}/${commentDetail.date.month}/${commentDetail.date.year}"
            date.text=string
        }

        private fun setStars(rate: Int) {
            val starList= arrayListOf(binding.starOne,binding.starTwo,binding.starThree,binding.starFour,binding.starFive)
            for (a in rate until 5){
                starList[a].setImageResource(R.drawable.ic_starborder)
            }
            for (a in rate downTo  1){
                starList[a-1].setImageResource(R.drawable.ic_starfull)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCommentAdapter.UserCommentViewHolder {
        val binding = OneusercommentlayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserCommentAdapter.UserCommentViewHolder, position: Int) {
        val createCommentNow = commentList[position]
        holder.setData(createCommentNow)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCommentList(newCommentList: List<Comments>) {
        commentList.clear()
        commentList.addAll(newCommentList)
        notifyDataSetChanged()
    }

}