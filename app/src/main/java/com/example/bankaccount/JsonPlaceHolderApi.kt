package com.example.bankaccount

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolderApi {
    @get:GET("labbbank/accounts")
    val accounts: Call<List<Account?>?>?
}