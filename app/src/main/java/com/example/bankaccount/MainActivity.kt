package com.example.bankaccount

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val clientName = "Hills";

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
    get()=
        @RequiresApi(Build.VERSION_CODES.P)
        object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication error: $errString")
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

        val lastNameTextView: TextView = findViewById(R.id.textView_lastName)

        findViewById<Button>(R.id.button_validation_id).setOnClickListener{
            if(lastNameTextView.text.toString()==clientName){
                val accountIntent = Intent(this, AccountActivity::class.java);
                startActivity(accountIntent);
            } else {
                Toast.makeText(this, "Wrong Name", Toast.LENGTH_LONG).show()
            }
        }

        checkBiometricSupport()

        startFingerprintPrompt()

        findViewById<AppCompatImageView>(R.id.fingerprint_ImageView).setOnClickListener{
            startFingerprintPrompt()
        }
    }

    private fun getCancellationSignal(): android.os.CancellationSignal {
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
        val biometricPrompt: android.hardware.biometrics.BiometricPrompt = android.hardware.biometrics.BiometricPrompt.Builder(this)
            .setTitle("Authentication is required")
            .setSubtitle("This app uses fingerprint to keep your data secure")
            .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener{_, _ ->
                notifyUser("Authentication cancelled")
            }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    private fun notifyUser(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
