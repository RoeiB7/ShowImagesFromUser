package com.example.showimagesfromuser.interfaces

interface DBCallBacks {
    fun firstInitDataCallBack(photoList: ArrayList<String>)
    fun newPhotoAddedCallBack(newPhotoList: ArrayList<String>)



}