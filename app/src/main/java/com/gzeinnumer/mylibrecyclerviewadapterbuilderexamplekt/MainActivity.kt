package com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.AdapterCreator
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.BuildAdapter
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt.databinding.ActivityMainBinding
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt.databinding.RvItemBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type1()
//        type2();
    }

    private fun type1() {
        val list: MutableList<MyModel> = ArrayList()
        for (i in 0..9) {
            list.add(MyModel(i, "Data Ke " + (i + 1)))
        }

        val adapter: AdapterCreator<MyModel> = BuildAdapter<MyModel>(R.layout.rv_item)
                .setCustomNoItem(R.layout.custom_empty_item)
                .setAnimation(R.anim.anim_two)
                .setList(list)
                .onBind { holder, position ->
                    val b = RvItemBinding.bind(holder)
                    b.btn.text = list[position].id.toString() + "_" + list[position].name
                    b.btn.setOnClickListener { Toast.makeText(this@MainActivity, "tekan $position", Toast.LENGTH_SHORT).show() }
                }
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.hasFixedSize()
        binding.rv.adapter = adapter

        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {

                for (i in 10..100) {
                    list.add(MyModel(i, "Data Ke " + (i + 1)))
                }
                adapter.setList(list)
            }
        }.start()
    }
}