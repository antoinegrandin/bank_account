package com.example.bankaccount

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Base64
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class MainActivity : AppCompatActivity() {

    val SHARED_PREFS = "sharedPrefs"
    val PASSWORD = "password"
    val SALT = "salt"
    val ALREADY_CONNECT = "already_connect"

    private var password: String? = null
    private var salt: String? = null
    private var already_connect = false

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
    get()=
        @RequiresApi(Build.VERSION_CODES.P)
        object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if(errorCode == 10){
                    notifyUser("Authentication cancelled")
                } else {
                    notifyUser("Authentication error: $errString")
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication success!")
                startActivity(Intent(this@MainActivity, AccountActivity::class.java))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()

        val passwordTextView: TextView = findViewById(R.id.textView_password)

        findViewById<Button>(R.id.button_validation_id).setOnClickListener{
            loadData()
            if(passwordTextView.text.toString()==password){
                val accountIntent = Intent(this, AccountActivity::class.java)
                startActivity(accountIntent)
            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
            }
        }

        checkBiometricSupport()

        startFingerprintPrompt()

        findViewById<AppCompatImageView>(R.id.fingerprint_ImageView).setOnClickListener{
            startFingerprintPrompt()
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        password = sharedPreferences.getString(PASSWORD, "0000")
        salt = sharedPreferences.getString(SALT, "")
        already_connect = sharedPreferences.getBoolean(ALREADY_CONNECT, false)
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }

        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if(!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        } else true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startFingerprintPrompt(){
        val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Authentication is required")
            .setSubtitle("This app uses fingerprint to keep your data secure")
            .setNegativeButton("Cancel", this.mainExecutor, { _, _ ->
                notifyUser("Authentication cancelled")
            }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    private fun notifyUser(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
