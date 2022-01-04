package com.softroniiks.digid.views.authentication.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softroniiks.digid.R;
import com.softroniiks.digid.views.authentication.AuthFragment;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        setTitle("Welcome");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.signup_container, AuthFragment.newInstance()).commitNow();
        }
    }
}