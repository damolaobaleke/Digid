package com.softroniiks.digid.views.authentication.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softroniiks.digid.R;
import com.softroniiks.digid.views.authentication.AuthFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.login_container, LoginFragment.newInstance()).commitNow();
        }
    }
}