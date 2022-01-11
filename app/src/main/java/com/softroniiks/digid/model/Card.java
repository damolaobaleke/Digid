package com.softroniiks.digid.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Card {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "card_id")
    int cardId;

    @ColumnInfo(name = "card_number")
    String cardNumber;
    @ColumnInfo(name = "expiry_date")
    String expiryDate;
    @ColumnInfo(name = "cvv")
    String cvv;
    @ColumnInfo(name = "full_name")
    String fullName;
    @ColumnInfo(name = "card_type")
    String cardType;
    @ColumnInfo(name = "card_issuer_id")
    int cardIssuerImageId;
    @ColumnInfo(name = "card_front_image_uri")
    String cardFrontImageUri;
    @ColumnInfo(name = "ownerId")
    public String ownerId;

    public Card(String cardNumber, String expiryDate, String cvv, String fullName, String cardType, int cardIssuerImageId,  String cardFrontImageUri){
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.fullName = fullName;
        this.cardType = cardType;
        this.cardIssuerImageId = cardIssuerImageId;
        this.cardFrontImageUri = cardFrontImageUri;

    }

    public void setOwnerId(String id){
        this.ownerId = id;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCardType() {
        return cardType;
    }

    public int getCardIssuerImageId() {
        return cardIssuerImageId;
    }

    public String getCardFrontImageUri() {
        return cardFrontImageUri;
    }
}
