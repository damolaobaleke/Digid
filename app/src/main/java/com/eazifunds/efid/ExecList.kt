package com.eazifunds.efid

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates


class ExecList : AppCompatActivity() {

    lateinit var execlist: ArrayList<String>
    var pos by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exec_list)
        setTitle("Eazifunds ID")

        val execlistview: ListView = findViewById(R.id.LV)

        execlist = ArrayList<String>()
        execlist.add(0, "Busayo Okusi")
        execlist.add(1, "Damola Obaleke")
        execlist.add(2, "Louis Olabalu")
        execlist.add(3, "Babajide Omisade")
        execlist.add(4, "Teslim Ogundiran")
        execlist.add(5, "Folajimi Ogunsankin")
        execlist.add(6, "Isaac Collins")
        execlist.add(7, "David Everett")
        execlist.add(8, "Solomon")

        val arrayadapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, execlist)
        execlistview.adapter = arrayadapter

        execlistview.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            pos = position
            when (position) {
                0 -> {
                    QRcodes()
                }
                1 -> {
                    QRcodes()
                }
                2 -> {
                    QRcodes()
                }
                3 -> {
                    QRcodes()
                }
                4 -> {
                    QRcodes()
                }
                5 -> {
                    QRcodes()
                }
                6 -> {
                    QRcodes()
                }
                7 -> {
                    QRcodes()
                }
                8 -> {
                    Log.i("List", "Solomon")
                }
            }
        }
    }

    fun QRcode1() {
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.qrcode_layout, null)
        val qr = view.findViewById<ImageView>(R.id.qrcode)
        qr.setImageResource(R.drawable.busayo)

        val testtext = view.findViewById<TextView>(R.id.test)
        testtext.setText("GET CONTACT")

        AlertDialog.Builder(this).setView(view).create().show()
    }

    @SuppressLint("SetTextI18n")
    fun QRcodes() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.qrcode_layout, null)
        val qr = view.findViewById<ImageView>(R.id.qrcode)

        val testtext = view.findViewById<TextView>(R.id.test)

        if (pos== 0) {
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.busayo)
        } else if (pos == 1) {
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.damola_obaleke)
        }else if(pos == 2){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.damola_obaleke)
        }else if(pos == 3){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.babjide)
        }else if(pos == 4){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.teslim)
        }else if(pos == 5){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.folajimi)
        }else if(pos == 6){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.collins)
        }else if(pos == 7){
            testtext.setText("Get contact details")
            qr.setImageResource(R.drawable.wolf)
        }else{
            return ;
        }

        AlertDialog.Builder(this).setView(view).create().show()
    }



    /**Getting image via uri*/
    /*val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
    val bitmap = ImageDecoder.decodeBitmap(source)
    imageView.setImageBitmap(bitmap)*/
}
