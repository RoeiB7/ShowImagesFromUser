package com.example.showimagesfromuser.objects

import com.example.showimagesfromuser.interfaces.DBCallBacks
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HandlerDB {
    private var db = Firebase.firestore
    private var firstDataRecycler = true
    private var photoList: ArrayList<String> = ArrayList()
    private var dbCallBacks: DBCallBacks? = null

    companion object {
        private const val  COLLECTION_PATH_IMAGES = "UriImages"

    }


    fun initDBCallBacks(dbCallBacks: DBCallBacks) {
        this.dbCallBacks = dbCallBacks

    }

    fun downLoadPhoto() {
        db.collection(COLLECTION_PATH_IMAGES)
            .addSnapshotListener { snapshot, e ->

                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    photoList.clear()
                    addItemsToArray(snapshot, photoList)

                    if (firstDataRecycler) {
                        dbCallBacks?.firstInitDataCallBack(photoList)
                        firstDataRecycler = false

                    } else {
                        dbCallBacks?.newPhotoAddedCallBack(photoList)

                    }

                } else {
                    return@addSnapshotListener
                }
            }


    }


    private fun addItemsToArray(snapshot: QuerySnapshot, photoList: ArrayList<String>) {
        for (document in snapshot.documentChanges) {
            val data = document.document.data
            val item = data["Uri"]
            photoList.add(item as String)
        }

    }

}