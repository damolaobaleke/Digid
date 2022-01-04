package com.softroniiks.digid.views.ids.ui.identity;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softroniiks.digid.model.AppDatabase;
import com.softroniiks.digid.model.DriverLicense;
import com.softroniiks.digid.model.UserAndDriverLicense;
import com.softroniiks.digid.utils.IdentityDao;
import com.softroniiks.digid.utils.UserDao;

import java.sql.Date;
import java.util.List;

public class IdentityViewModel extends ViewModel {

    private UserDao userDao;
    private IdentityDao identityDao;
    private AppDatabase db;
    private LiveData<UserAndDriverLicense> userAndDriverLicense;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    public IdentityViewModel() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void initializeDb(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "softroniiks-wallet-db").build();
        userDao = db.userDao();
        identityDao = db.identityDao();
    }


    //create
    public void addIdentity(DriverLicense driverLicense) {
        identityDao.addIdentityDl(driverLicense);
    }

    //update
    public void updateIdentity(DriverLicense driverLicense) {
        identityDao.updateIdentityDl(driverLicense);
    }

    //read
    public LiveData<UserAndDriverLicense> getUserWithDl() {
        String userId = "";
        if (user != null) {
            userId = user.getUid();
        }
        return userDao.getUserWithDriverLicense(userId);
    }

    //delete
    public void deleteIdentity(DriverLicense driverLicense) {
        identityDao.deleteIdentityDl(driverLicense);
    }
}