package com.softroniiks.digid.views.ids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softroniiks.digid.R;
import com.softroniiks.digid.views.authentication.login.LoginActivity;
import com.softroniiks.digid.views.authentication.login.LoginFragment;
import com.softroniiks.digid.views.authentication.signup.SignUpActivity;

public class IdNavigationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_id_navigation);

        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_card, R.id.navigation_identity, R.id.navigation_invoices).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_id_navigation);
        //TODO: comment actionbar setup, find another position to place log out
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();

                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}