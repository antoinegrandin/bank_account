package com.example.bankaccount

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AccountActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bankaccount)

        val db = DataBaseHandler(this)

        val textViewResult: TextView = findViewById(R.id.text_view_result)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://60102f166c21e10017050128.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call: Call<List<Account>> = jsonPlaceHolderApi.accounts

        call.enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                if (!response.isSuccessful) {
                    val errorMessage = "Code : $response.code()"
                    textViewResult.text = errorMessage
                    return
                }

                db.cleanData()

                val accounts: List<Account> = response.body() as List<Account>

                for (account in accounts) {
                    db.insertData(account)

                    var content = ""
                    content += """
                        ID: ${account.id}
                        
                        """.trimIndent()
                    content += """
                        Account Name: ${account.accountName}
                        
                        """.trimIndent()
                    content += """
                        Amount: ${account.amount}
                        
                        """.trimIndent()
                    content += """
                        Iban: ${account.iban}
                        
                        """.trimIndent()
                    content += """
                        Currency: ${account.currency}
                        
                        
                        """.trimIndent()
                    textViewResult.append(content)
                }
            }

            override fun onFailure(call: Call<List<Account>>, throwable: Throwable) {
                val accounts: List<Account> = db.readData()
                val connectionErrorMessage = "No Connection...Display of the latest available data : \n\n"
                textViewResult.text = connectionErrorMessage
                for (account in accounts) {
                    var content = ""
                    content += """
                        ID: ${account.id}
                        
                        """.trimIndent()
                    content += """
                        Account Name: ${account.accountName}
                        
                        """.trimIndent()
                    content += """
                        Amount: ${account.amount}
                        
                        """.trimIndent()
                    content += """
                        Iban: ${account.iban}
                        
                        """.trimIndent()
                    content += """
                        Currency: ${account.currency}
                        
                        
                        """.trimIndent()
                    textViewResult.append(content)
                }
            }
        })


    }
}
