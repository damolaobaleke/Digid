package com.softroniiks.digid.utils;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.softroniiks.digid.model.DriverLicense;

@Dao
public interface IdentityDao {

    @Insert
    void addIdentityDl(DriverLicense... driverLicenses);

    @Update
    void updateIdentityDl(DriverLicense driverLicense);

    @Delete
    void deleteIdentityDl(DriverLicense driverLicense);

}
