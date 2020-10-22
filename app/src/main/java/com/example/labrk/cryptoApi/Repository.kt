package com.example.labrk.cryptoApi

import com.example.labrk.enums.Crypto
import com.example.labrk.enums.Money
import io.reactivex.Single

class Repository (apiKey: String) {
    private val injector = Injector(apiKey)
    private val service: Service by injector.instance()

    fun getHistory(fsym: Crypto? = null, tsym: Money? = null, limit: Int? = null): Single<Answer> {
        return service.getHistory(fsym, tsym, limit)
    }
}