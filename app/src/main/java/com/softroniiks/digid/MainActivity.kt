package com.softroniiks.digid
/**Written by Oyindamola Obaleke
 * 1/14/2020
 * All rights reserved Softroniiks inc*/
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.softroniiks.digid.views.authentication.signup.SignUpActivity

class MainActivity : AppCompatActivity() {

    private var SPLASH_TIME_OUT: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val runnable = Runnable {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val handler = Handler()
        handler.postDelayed(runnable, SPLASH_TIME_OUT)


    }
}
