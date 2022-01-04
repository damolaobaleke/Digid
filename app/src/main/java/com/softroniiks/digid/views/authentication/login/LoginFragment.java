package com.softroniiks.digid.views.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softroniiks.digid.R;
import com.softroniiks.digid.views.authentication.AuthViewModel;
import com.softroniiks.digid.views.authentication.signup.SignUpActivity;
import com.softroniiks.digid.views.ids.IdNavigationActivity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {
    TextInputEditText editTextPassword, editTextEmail;
    TextInputLayout layoutFullName;
    Button signUp, logIn;
    Pattern pattern;
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+_])(?=\\S+$).{8,16}$";
    private AuthViewModel authViewModel;
    FirebaseUser currentUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = mAuth.getCurrentUser();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.initializeDb(requireContext());

        pattern = Pattern.compile(regex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment, container, false);

        editTextEmail = view.findViewById(R.id.emailEditText);
        editTextPassword = view.findViewById(R.id.passwordEditText);
        layoutFullName = view.findViewById(R.id.outlinedTextFieldFullName);
        signUp = view.findViewById(R.id.signUpButton);
        logIn = view.findViewById(R.id.loginButton);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutFullName.setVisibility(View.GONE);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SignUpActivity.class);
            startActivity(intent);
        });

        //login
        logIn.setOnClickListener(v -> login());
    }

    public void login() {
        if (TextUtils.isEmpty(editTextEmail.getText()) || TextUtils.isEmpty(editTextPassword.getText())) {
            Toast.makeText(requireContext(), "Input's are empty", Toast.LENGTH_SHORT).show();
        } else {
            boolean isLoggedIn = authViewModel.logIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());

            if (isLoggedIn) {
                Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireContext(), IdNavigationActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(requireContext(), "Failed to Log in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoggedIn(currentUser);
    }

    public void checkLoggedIn(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(requireContext(), IdNavigationActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        }
    }

}