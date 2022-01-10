package com.softroniiks.digid.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndCard {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "ownerId"
    )
    public List<Card> cards;
}
