package com.example.labrk.cryptoApi

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface Service {
    @GET("data/v2/histoday")
    fun getHistory(@Query("fsym") fsym: String,
                   @Query("tsym") tsym: String,
                   @Query("limit") limit: Int?): Single<Answer>
}
