package com.softroniiks.digid.model;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.softroniiks.digid.model.DriverLicense;
import com.softroniiks.digid.model.User;
import com.softroniiks.digid.utils.Converters;
import com.softroniiks.digid.utils.IdentityDao;
import com.softroniiks.digid.utils.UserDao;

@Database(entities = {User.class, DriverLicense.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract IdentityDao identityDao();
}
