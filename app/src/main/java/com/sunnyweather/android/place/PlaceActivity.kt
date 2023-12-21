package com.sunnyweather.android.place
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R

class PlaceActivity : AppCompatActivity(){

    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var et_searchPlace: EditText

    private lateinit var iv_bj: ImageView


    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)



        recyclerView = findViewById(R.id.recyclerView)
        et_searchPlace = findViewById(R.id.et_searchPlace)
        iv_bj = findViewById(R.id.iv_bj)
        //获取searchPlaceEdit焦点，弹出软键盘
        iv_bj.requestFocus()


        //设置地点搜索的RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter = adapter

        et_searchPlace.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlace(content)
            } else {
                recyclerView.visibility = View.GONE
                iv_bj.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                iv_bj.visibility = View.GONE
                viewModel.placeList.apply {
                    clear()
                    addAll(places)}
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}