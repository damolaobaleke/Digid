package com.softroniiks.digid.utils;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.softroniiks.digid.model.Card;

@Dao
public interface CardDao {

    @Insert
    void storeCard(Card... card);

    @Delete
    void deleteCard(Card card);

}
