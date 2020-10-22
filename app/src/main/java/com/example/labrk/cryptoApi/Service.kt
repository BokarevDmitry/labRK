package com.example.labrk.cryptoApi

import com.example.labrk.enums.Crypto
import com.example.labrk.enums.Money
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface Service {

    @GET("data/v2/histoday")
    fun getHistory(@Query("fsym") fsym: Crypto?,
                   @Query("tsym") tsym: Money?,
                   @Query("limit") limit: Int?): Single<Answer>
}
