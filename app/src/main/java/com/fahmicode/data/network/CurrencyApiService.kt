package com.fahmicode.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import com.squareup.moshi.Json

data class ExchangeRateResponse(
    val result: String,
    @Json(name = "time_last_update_utc") val lastUpdateUtc: String,
    @Json(name = "base_code") val baseCode: String,
    @Json(name = "rates") val rates: Map<String, Double>
)

interface CurrencyApiService {
    @GET("v6/latest/USD")
    suspend fun getUsdExchangeRates(): ExchangeRateResponse

    companion object {
        private const val BASE_URL = "https://open.er-api.com/"

        fun create(): CurrencyApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(CurrencyApiService::class.java)
        }
    }
}
