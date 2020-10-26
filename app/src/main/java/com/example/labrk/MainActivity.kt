package com.example.labrk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.labrk.cryptoApi.Record
import com.example.labrk.cryptoApi.Repository
import com.example.labrk.databinding.ActivityMainBinding
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {
    private val API_KEY = "4f9366a4993b0698da075875200877a598a73c430d397e3304564dd2b2d4186b"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        binding.swipeContainer.setOnRefreshListener(this)
        binding.swipeContainer.setColorScheme(R.color.green, R.color.red, R.color.purple_700)

        setSpinners()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val limit = prefs.getString("limit", "10")?.toInt()

        val fsym = binding.spinnerCrypto.selectedItem.toString()
        val tsym = binding.spinnerMoney.selectedItem.toString()

        binding.infoChosen.text = "Курс за $limit дней пара $fsym/$tsym"

        val adapter = Adapter(getRecords(limit = limit?.minus(1), fsym = fsym, tsym = tsym), object : Adapter.Callback {
            override fun onItemClicked(item: Record) {
                openDetailedInfo(item)
            }
        })
        binding.recyclerview.adapter = adapter
    }

    private fun setSpinners() {
        val dataAdapterCrypto = ArrayAdapter.createFromResource(this, R.array.cryptoNames, android.R.layout.simple_spinner_item)
        dataAdapterCrypto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCrypto.adapter = dataAdapterCrypto
        binding.spinnerCrypto.onItemSelectedListener = this

        val dataAdapterMoney = ArrayAdapter.createFromResource(this, R.array.moneyNames, android.R.layout.simple_spinner_item)
        dataAdapterCrypto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMoney.adapter = dataAdapterMoney
        binding.spinnerMoney.onItemSelectedListener = this
    }

    private fun openDetailedInfo(item: Record) {
        val intent = Intent(this, ChildActivity::class.java)
        intent.putExtra("time", item.time)
                .putExtra("open", item.open)
                .putExtra("close", item.close)
                .putExtra("low", item.low)
                .putExtra("high", item.high)
                .putExtra("volumeFrom", item.volumeFrom)
                .putExtra("volumeTo", item.volumeTo)
        startActivity(intent)
    }

    private fun getRecords(limit: Int?, fsym: String, tsym: String): List<Record> {
        val repository = Repository(API_KEY)
        return repository.getHistory(fsym = fsym, tsym = tsym, limit = limit)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { answer -> answer.days.records }
            .toList()
            .blockingGet()
                .reversed()
    }

    override fun onRefresh() {
        binding.swipeContainer.isRefreshing = true
        binding.swipeContainer.postDelayed({
            run() {
                val fsym = binding.spinnerCrypto.selectedItem.toString()
                val tsym = binding.spinnerMoney.selectedItem.toString()

                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val limit = prefs.getString("limit", "10")?.toInt()
                val adapter = Adapter(getRecords(limit = limit?.minus(1), fsym = fsym, tsym = tsym), object : Adapter.Callback {
                    override fun onItemClicked(item: Record) {
                        openDetailedInfo(item)
                    }
                })
                binding.swipeContainer.isRefreshing = false
                binding.recyclerview.adapter = adapter
                binding.infoChosen.text = "Курс за $limit дней пара $fsym/$tsym"
            }
        }, 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> run {
                val settingsActivity = Intent(baseContext, SettingsActivity::class.java)
                startActivity(settingsActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        onRefresh()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}