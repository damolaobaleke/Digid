package com.softroniiks.digid.model;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//modeling 1:many relationship  user: id's
public class UserAndDriverLicense {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "ownerId"
    )
    public List<DriverLicense> driverLicense;
}
