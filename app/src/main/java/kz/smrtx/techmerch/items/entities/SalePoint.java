
package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "ST_SALEPOINT")
public class SalePoint implements Serializable{
    @PrimaryKey
    @NonNull
    @SerializedName("SAL_CODE")
    @Expose
    private int code;

    @SerializedName("SAL_ID")
    @Expose
    private String id;
    @SerializedName("SAL_NAME")
    @Expose
    private String name;
    @SerializedName("SAL_STREET")
    @Expose
    private String street;
    @SerializedName("SAL_LOC_CODE")
    @Expose
    private int locationCode;
    @SerializedName("SAL_LATITUDE")
    @Expose
    private String latitude;
    @SerializedName("SAL_LONGITUDE")
    @Expose
    private String longitude;
    @SerializedName("SAL_CONTACT")
    @Expose
    private String contact;
    @SerializedName("SAL_NOTES")
    @Expose
    private String note;
    @SerializedName("SAL_LEGAL_ENTITY")
    @Expose
    private String legalEntity;
    @SerializedName("SAL_CHANNEL")
    @Expose
    private String channel;
    @SerializedName("SAL_CATEGORY")
    @Expose
    private String category;
    @SerializedName("SAL_TYPE")
    @Expose
    private String type;
    @SerializedName("USE_CODE")
    @Expose
    private int userCode;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
}
