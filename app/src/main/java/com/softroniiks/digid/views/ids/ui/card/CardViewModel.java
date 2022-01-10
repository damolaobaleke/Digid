package com.softroniiks.digid.views.ids.ui.card;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;
import com.softroniiks.digid.model.AppDatabase;
import com.softroniiks.digid.model.Card;
import com.softroniiks.digid.model.UserAndCard;
import com.softroniiks.digid.utils.CardDao;
import com.softroniiks.digid.utils.UserDao;

public class CardViewModel extends ViewModel {
    CardDao cardDao;
    UserDao userDao;
    AppDatabase db;
    LiveData<UserAndCard> userAndCard;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public CardViewModel() {

    }

    public void initializeDb(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "softroniiks-wallet-db").build();
        cardDao = db.cardDao();
        userDao = db.userDao();
    }

    public void addCard(Card card){
        cardDao.storeCard(card);
    }

    public LiveData<UserAndCard> getUserCards(){
        if(mAuth.getCurrentUser() != null) {
            return userDao.getUserWithCards(mAuth.getUid());
        }else{
            return null;
        }
    }

    public void deleteCard(Card card){
        cardDao.deleteCard(card);
    }

}