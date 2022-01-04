package com.softroniiks.digid.utils;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.softroniiks.digid.model.DriverLicense;
import com.softroniiks.digid.model.User;
import com.softroniiks.digid.model.UserAndDriverLicense;

import java.util.List;

@Dao
public interface UserDao {

    @Insert()
    void addUser(User... users);

    @Query("SELECT * FROM User Where userid = :id")
    LiveData<User> getUserViaId(String id);

    @Transaction
    @Query("SELECT * FROM User Where userid = :id")
    LiveData<UserAndDriverLicense> getUserWithDriverLicense(String id);

    @Query("DELETE FROM DriverLicense")
    void deleteAllLicenses();

    //Update

    //
    @Delete()
    void deleteID(DriverLicense driverLicense);

}
