package com.softroniiks.digid.views.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softroniiks.digid.R;
import com.softroniiks.digid.views.authentication.login.LoginActivity;
import com.softroniiks.digid.views.ids.IdNavigationActivity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AuthFragment extends Fragment {
    TextInputEditText editTextPassword, editTextEmail, editTextFullName;
    Button signUp, logIn;
    Pattern pattern;
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+_])(?=\\S+$).{8,16}$";
    private AuthViewModel authViewModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = mAuth.getCurrentUser();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.initializeDb(requireContext());

        pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+_])(?=\\S+$).{8,16}$");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.auth_fragment, container, false);

        editTextEmail = view.findViewById(R.id.emailEditText);
        editTextPassword = view.findViewById(R.id.passwordEditText);
        editTextFullName =  view.findViewById(R.id.fullNameEditText);
        signUp =  view.findViewById(R.id.signUpButton);
        logIn =  view.findViewById(R.id.loginButton);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUp.setOnClickListener(v->{
            if(TextUtils.isEmpty(editTextEmail.getText()) || TextUtils.isEmpty(editTextPassword.getText())){
                Toast.makeText(requireContext(), "Input's are empty", Toast.LENGTH_SHORT).show();
            }

            Matcher matcher = pattern.matcher(Objects.requireNonNull(editTextPassword.getText().toString().trim()));
            boolean isMatch = matcher.matches();

            System.out.println(isMatch);

            if(!isMatch){
                Log.i("", "Password doesn't meet requirements");
                Toast.makeText(requireContext(), "Password doesn't meet requirements", Toast.LENGTH_SHORT).show();

            } else {
                boolean isSignedUp = authViewModel.signUp(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextFullName.getText().toString().trim());

                if (isSignedUp) {
                    Toast.makeText(requireContext(), "Sign up successful\nnow login", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Replace error message
                    Toast.makeText(requireContext(), authViewModel.failureMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //login
        logIn.setOnClickListener(v->login());
    }

    public void signUp(){

    }

    public void login(){
        Intent intent  = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoggedIn(user);
    }

    private void checkLoggedIn(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(requireContext(), IdNavigationActivity.class);
            intent.putExtra("user", user.getEmail()+" ");
            startActivity(intent);
        }
    }

    //TODO: test
    public boolean sqlUserExists() {
        if (authViewModel.getUser(user.getUid()).getValue() != null) {
            Log.i("Login", authViewModel.getUser(user.getUid()).getValue().getEmail()+" ");
            return true;
        } else return false;
    }
}