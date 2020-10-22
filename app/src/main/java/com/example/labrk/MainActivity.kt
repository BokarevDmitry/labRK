package com.example.labrk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labrk.cryptoApi.Answer
import com.example.labrk.cryptoApi.Record
import com.example.labrk.cryptoApi.Repository
import com.example.labrk.databinding.ActivityMainBinding
import com.example.labrk.enums.Crypto
import com.example.labrk.enums.Money
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val API_KEY = "4f9366a4993b0698da075875200877a598a73c430d397e3304564dd2b2d4186b"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        //val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        //val category = prefs.getString("category", "")
        val adapter = Adapter(getRecords(), object : Adapter.Callback {
            override fun onItemClicked(item: Record) {
                TODO("Not yet implemented")
            }
        })
        binding.recyclerview.adapter = adapter
    }

    private fun getRecords(): List<Record> {
        val repository = Repository(API_KEY)
        return repository.getHistory(fsym = Crypto.BTC, tsym = Money.USD, limit = 10)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { answer -> answer.days.records }
            .toList()
            .blockingGet()
    }
}