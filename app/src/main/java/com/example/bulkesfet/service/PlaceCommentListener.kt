package com.example.bulkesfet.service

import com.example.bulkesfet.model.Comments

interface PlaceCommentListener {
    fun setVisibility()
    fun refreshRecycler(newCommentList: ArrayList<Comments>)
}