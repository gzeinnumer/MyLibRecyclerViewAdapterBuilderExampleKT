package com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt.databinding.ActivityMainBinding
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt.databinding.RvItemBinding
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexamplekt.databinding.RvItemGenapBinding
import com.gzeinnumer.rab.helper.BindViewHolderMultiType
import com.gzeinnumer.rab.model.TypeViewItem
import com.gzeinnumer.rab.multiType.AdapterBuilderMultiType
import com.gzeinnumer.rab.multiType.AdapterCreatorMultiType
import com.gzeinnumer.rab.singleType.AdapterBuilder
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        typeSingle()
//        typeMulti()
    }

    private fun typeSingle() {
        val list: MutableList<MyModel> = ArrayList()
        for (i in 0..9) {
            list.add(MyModel(i, "Data Ke " + (i + 1)))
        }

        val adapter = AdapterBuilder<MyModel>(R.layout.rv_item)
            .setList(list)
            .setCustomNoItem(R.layout.custom_empty_item)
            .setAnimation(R.anim.anim_two)
            .setDivider(R.layout.custom_divider)
            .onBind { adapter, holder, data, position ->
                //rv_item = RvItemBinding
                val bindingItem = RvItemBinding.bind(holder)
                bindingItem.btn.text = data.id.toString() + "_" + data.name
                bindingItem.btn.setOnClickListener {
                    Toast.makeText(
                        this@MainActivity,
                        "tekan $position",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .onFilter { constraint, listFilter ->
                val fildteredList: MutableList<MyModel> = ArrayList()

                if (constraint.isNotEmpty()) {
                    val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in listFilter) {
                        if (item.id.toString().toLowerCase().contains(filterPattern)) {
                            fildteredList.add(item)
                        }
                        if (item.name.toString().toLowerCase().contains(filterPattern)) {
                            fildteredList.add(item)
                        }
                    }
                }
                fildteredList
            }

        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.hasFixedSize()

        //after 5 second, new data will appear
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                for (i in 10..100) {
                    list.add(MyModel(i, "Data Ke " + (i + 1)))
                }
                adapter.setList(list)
            }
        }.start()

        //use filter on TextWacher
        binding.ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                //call the filter
                adapter.filter.filter(s)
            }
        })
    }

    private fun typeMulti() {
        val list: MutableList<MyModel> = ArrayList()
        for (i in 0..9) {
            list.add(MyModel(i, "Data Ke " + (i + 1)))
        }

        val adapter = AdapterBuilderMultiType<MyModel>()
            .setList(list)
            .setCustomNoItem(R.layout.custom_empty_item)
            .setAnimation(R.anim.anim_two)
            .setDivider(R.layout.custom_divider)
            .onBind(object : BindViewHolderMultiType<MyModel> {

                private val TYPE_GENAP = 1
                private val TYPE_GANJIL = 0

                override fun getItemViewType(position: Int): TypeViewItem {
                    return if (position % 2 == 0)
                        TypeViewItem(TYPE_GENAP, R.layout.rv_item_genap)
                    else
                        TypeViewItem(TYPE_GANJIL, R.layout.rv_item)
                }

                override fun bind(adapter: AdapterCreatorMultiType<MyModel>, holder: View, data: MyModel, position: Int, viewType: Int) {
                    if (viewType == TYPE_GENAP) {
                        val bindingItem = RvItemGenapBinding.bind(holder)
                        bindingItem.btn.text = data.id.toString() + "_" + data.name + "_Genap"
                    } else if (viewType == TYPE_GANJIL) {
                        val bindingItem = RvItemBinding.bind(holder)
                        bindingItem.btn.text = data.id.toString() + "_" + data.name + "_Ganjil"
                    }
                }

            })
            .onFilter { constraint, listFilter ->
                val fildteredList: MutableList<MyModel> = ArrayList()

                if (constraint.isNotEmpty()) {
                    val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in listFilter) {
                        if (item.id.toString().toLowerCase().contains(filterPattern)) {
                            fildteredList.add(item)
                        }
                        if (item.name.toLowerCase().contains(filterPattern)) {
                            fildteredList.add(item)
                        }
                    }
                }
                fildteredList
            }

        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.hasFixedSize()

        //after 5 second, new data will appear
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                for (i in 10..100) {
                    list.add(MyModel(i, "Data Ke " + (i + 1)))
                }
                adapter.setList(list)
            }
        }.start()

        //use filter on TextWacher
        binding.ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                //call the filter
                adapter.filter.filter(s)
            }
        })
    }
}