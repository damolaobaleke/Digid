package com.softroniiks.digid.views.authentication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.softroniiks.digid.model.User;
import com.softroniiks.digid.model.AppDatabase;
import com.softroniiks.digid.utils.UserDao;

import java.util.concurrent.TimeUnit;

public class AuthViewModel extends ViewModel {
    AppDatabase db;
    UserDao userDao;
    //FbAuth
    FirebaseAuth mAuth;
    FirebaseUser user;
    boolean isSignedInWithPhoneNumber, isLoggedin,isSignedUp;
    private static final String TAG = "AuthViewModel";
    public String errMsgLgn ,failureMsg = "";


    public AuthViewModel(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void initializeDb(Context context){
        db = Room.databaseBuilder(context, AppDatabase.class, "softroniiks-wallet-db").build();
        userDao = db.userDao();
    }

    public boolean signUp(final String email, final String password, String fullName){
        if(email.equals("") || password.equals("")){
           Log.i(TAG, "Input's are empty");
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                user = authResult.getUser();
                assert user != null;

                Log.i(TAG, user.getEmail() + " Signed In");
                if (user != null) {
                    isSignedUp = true;

                    AsyncTask.execute(() -> {
                        storeInDb(user.getUid(), email.trim(), fullName.trim(), String.valueOf(password.trim().hashCode()));
                    });
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                failureMsg = e.getLocalizedMessage();
                isSignedUp = false;
            }
        });

        return isSignedUp;
    }

    public boolean logIn(String email, String password){
         mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(authResult.getUser() != null){
                    isLoggedin = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isLoggedin = false;
                errMsgLgn = e.getMessage();
            }
        });

        return isLoggedin;
    }

    public void storeInDb(String id, String email, String fullName,String password){
        User user = new User(id, email, fullName ,password);
        userDao.addUser(user);
    }


    public void signInWithPhoneNumber(String phoneNumber, Activity activity){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {// OnVerificationStateChangedCallbacks
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.i(TAG, e.getLocalizedMessage());
                            }
                        }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private boolean signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if(user != null){
                                isSignedInWithPhoneNumber = true;
                            }

                            isSignedInWithPhoneNumber = false;

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e(TAG, "Wrong Credentials");
                                isSignedInWithPhoneNumber = false;
                            }
                        }
                    }
                });
        return isSignedInWithPhoneNumber;
    }

    public LiveData<User> getUser(String userId){

        return userDao.getUserViaId(userId);
    }

}