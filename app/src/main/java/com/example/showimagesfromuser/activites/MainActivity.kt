package com.example.showimagesfromuser.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.showimagesfromuser.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.showimagesfromuser.interfaces.DBCallBacks
import com.example.showimagesfromuser.recyclerViews.PhotoRecyclerAdapter
import com.example.showimagesfromuser.objects.HandlerDB


class MainActivity : AppCompatActivity(), DBCallBacks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotoRecyclerAdapter
    private lateinit var allPhotoList: ArrayList<String>
    private lateinit var db: FirebaseFirestore
    private lateinit var newPhotos: ArrayList<String>
    private lateinit var handlerDB: HandlerDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initValues()
        initCallBacks()
        initListeners()
        handlerProgressBar(View.VISIBLE)


        Thread {
            handlerDB.downLoadPhoto()
        }.start()

        initRecyclerView()
        divideRowsInRecyclerView()


    }

    private fun handlerProgressBar(value: Int) {
        binding.mainProgressBarRecycler.visibility = value

    }

    // make line in recyclerView between photos
    private fun divideRowsInRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(
            binding.mainRecyclerPhoto.context,
            LinearLayoutManager.VERTICAL
        )
        binding.mainRecyclerPhoto.addItemDecoration(dividerItemDecoration)
    }

    private fun initRecyclerView() {
        binding.mainRecyclerPhoto.layoutManager = LinearLayoutManager(this)
        adapter =
            PhotoRecyclerAdapter(
                this,
                allPhotoList
            )
        binding.mainRecyclerPhoto.adapter = adapter
    }

    private fun initListeners() {
        binding.mainRefresh.setOnRefreshListener(refreshRecyclerView)

    }

    private val refreshRecyclerView = SwipeRefreshLayout.OnRefreshListener {
        binding.mainRefresh.isRefreshing = true
        addItemsToAllPhotoList()
        newPhotos.clear()


    }

    private fun initCallBacks() {
        handlerDB.initDBCallBacks(this)
    }

    private fun initValues() {
        allPhotoList = ArrayList()
        newPhotos = ArrayList()
        db = Firebase.firestore
        handlerDB = HandlerDB()
    }


    private fun addItemsToAllPhotoList() {
        allPhotoList.addAll(0, newPhotos)
        notifyItemsAdded(0, newPhotos.size)
        binding.mainRefresh.isRefreshing = false
    }


    private fun notifyItemsAdded(positionStart: Int, photoList: Int) {
        runOnUiThread {
            adapter.notifyItemRangeInserted(positionStart, photoList)
        }

    }

    // when user entered to application download all the photo in DB
    override fun firstInitDataCallBack(photoList: ArrayList<String>) {
        runOnUiThread {
            allPhotoList.addAll(0, photoList)
            notifyItemsAdded(0, photoList.size)
            handlerProgressBar(View.INVISIBLE)
        }
    }

    // when the user take new photo
    override fun newPhotoAddedCallBack(newPhotoList: ArrayList<String>) {
        runOnUiThread {
            newPhotos.addAll(0, newPhotoList)
            Toast.makeText(this, "you have new ${newPhotos.size} photo ", Toast.LENGTH_LONG).show()
        }
    }


}