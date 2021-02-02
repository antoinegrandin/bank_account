package com.example.bankaccount

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bankaccount)

        var textViewResult: TextView = findViewById(R.id.text_view_result);

        val retrofit = Retrofit.Builder()
            .baseUrl("https://60102f166c21e10017050128.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java);

        val call: Call<List<Post>> = jsonPlaceHolderApi.posts;

        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    textViewResult.text = "Code : " + response.code();
                    return;
                }

                val posts: List<Post> = response.body() as List<Post>;

                for (post in posts) {
                    var content = ""
                    content += """
                        ID: ${post.id}
                        
                        """.trimIndent()
                    content += """
                        Account Name: ${post.accountName}
                        
                        """.trimIndent()
                    content += """
                        Amount: ${post.amount}
                        
                        """.trimIndent()
                    content += """
                        Iban: ${post.iban}
                        
                        """.trimIndent()
                    content += """
                        Currency: ${post.currency}
                        
                        
                        """.trimIndent()
                    textViewResult.append(content)
                }
            }

            override fun onFailure(call: Call<List<Post>>, throwable: Throwable) {
                textViewResult.text = throwable.toString();
            }
        })
    }
}
