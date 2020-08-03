package com.eazifunds.efid;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Conversion extends AppCompatActivity {

    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);


        ListView execlistview = findViewById(R.id.LV);

        ArrayList<String> execlist = new ArrayList<String>();
        execlist.add(0, "Busayo Okusi");
        execlist.add(1, "Damola Obaleke");
        execlist.add(2, "Louis Olabalu");
        execlist.add(3, "Babajide Omisade");
        execlist.add(4, "Teslim Ogundiran");
        execlist.add(5, "Folajimi Ogunsankin");
        execlist.add(6, "Isaac Collins");
        execlist.add(7, "David Everett");
        execlist.add(8, "Solomon");

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, execlist);
        execlistview.setAdapter(arrayadapter);

        execlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                switch (position) {
                    case 1:
                        QRcode();
                        break;
                    case 2:
                        QRcode();
                        break;

                }
            }
        });
    }

    public void Test(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = LayoutInflater.from(this);
        final View view2 = inflater2.inflate(R.layout.qrcode_layout, null);
        dialog.setView(view2);


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.qrcode_layout, null);
        ImageView qr = view.findViewById(R.id.qrcode);
        qr.setImageResource(R.drawable.babjide);
        new AlertDialog.Builder(this).setView(view).create().show();

        qr.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.busayo));
    }

    public void QRcode() {
        int position = this.pos;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.qrcode_layout, null);
        ImageView qr = view.findViewById(R.id.qrcode);

        if(position == 0){
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.busayo);
            qr.setImageResource(R.drawable.busayo);
        }else if(position == 1) {
            qr.setImageResource(R.drawable.damola_obaleke);
        }

        new AlertDialog.Builder(this).setView(view).create().show();


    }

}
