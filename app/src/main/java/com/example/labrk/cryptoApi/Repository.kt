package com.example.labrk.cryptoApi

import io.reactivex.Single

class Repository (apiKey: String) {
    private val injector = Injector(apiKey)
    private val service: Service by injector.instance()

    fun getHistory(fsym: String = "", tsym: String = "", limit: Int? = null): Single<Answer> {
        return service.getHistory(fsym, tsym, limit)
    }
}