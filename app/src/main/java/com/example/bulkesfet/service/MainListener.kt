package com.example.bulkesfet.service

import java.io.File

interface MainListener {
    fun showOrHide(value:Boolean)
    fun getFilesDirBenim(): File
}