package com.example.bankaccount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val clientName = "Hills";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onStart() {
        super.onStart()

        val lastNameTextView: TextView = findViewById(R.id.textView_lastName)

        findViewById<Button>(R.id.button_validation_id).setOnClickListener{
            if(lastNameTextView.text.toString()==clientName){
                val accountIntent = Intent(this, AccountActivity::class.java);
                startActivity(accountIntent);
            } else {
                Toast.makeText(this, "Wrong Name", Toast.LENGTH_LONG).show()
            }
        }
    }
}