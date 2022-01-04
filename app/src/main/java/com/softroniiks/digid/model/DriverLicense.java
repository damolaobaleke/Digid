package com.softroniiks.digid.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(
        foreignKeys = {
                @ForeignKey(
                    entity = User.class,
                    childColumns = "ownerId",
                    parentColumns = "userId",
                    onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("ownerId")}
)
public class DriverLicense {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int dlUid;

    @ColumnInfo(name = "date_of_birth")
    private java.sql.Date dob;
    @ColumnInfo(name = "date_of_expiry")
    private java.sql.Date doe;
    @ColumnInfo(name = "date_of_issue")
    private java.sql.Date doi;
    @ColumnInfo(name = "documentNumber")
    private final String documentNumber;
    @ColumnInfo(name = "address")
    private final String address;
    @ColumnInfo(name = "firstName")
    private final String firstName;
    @ColumnInfo(name = "lastName")
    private final String lastName;
    @ColumnInfo(name = "sex")
    private final String sex;
    @ColumnInfo(name = "vehicleClass")
    private final String vehicleClass;
    @ColumnInfo(name = "ownerId")
    private String ownerId;

    public DriverLicense(Date dob, Date doe, Date doi, String documentNumber, String firstName, String lastName, String sex, String vehicleClass, String address) {
        this.dob = dob;
        this.doe = doe;
        this.doi = doi;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.vehicleClass = vehicleClass;
        this.address = address;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setDlUid(int dlUid) {
        this.dlUid = dlUid;
    }

    public Date getDob() {
        return dob;
    }

    public Date getDoe() {
        return doe;
    }

    public Date getDoi() {
        return doi;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public int getDlUid() {
        return dlUid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getAddress() {
        return address;
    }
}

