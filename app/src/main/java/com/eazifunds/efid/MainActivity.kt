package com.eazifunds.efid
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

class MainActivity : AppCompatActivity() {

    private var SPLASH_TIME_OUT: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eflogo: ImageView = findViewById(R.id.eflogo)
        eflogo.setImageResource(R.drawable.eflogo)
        eflogo.animate().setInterpolator(AccelerateDecelerateInterpolator())
        eflogo.animate().rotation(3000f).setDuration(3600).start()

        val text: TextView = findViewById(R.id.eazi)
        text.animate().alpha(1f).startDelay = 2000
        text.animate().scaleX(1.4f).scaleY(1.4f).start()

        val progressbar: ProgressBar = findViewById(R.id.progressBar)
        progressbar.interpolator = AccelerateDecelerateInterpolator()
        Handler().postDelayed({ progressbar.visibility = View.GONE }, SPLASH_TIME_OUT)

        val runnable = Runnable {
            val intent = Intent(this,AdminLogin::class.java)
            startActivity(intent)
        }

        val handler = Handler()
        handler.postDelayed(runnable, SPLASH_TIME_OUT)


    }
}
