
package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
    @SerializedName("SAL_NOTES")
    @Expose
    private String notes;
    @SerializedName("SAL_HOUSE")
    @Expose
    private String house;
    @SerializedName("SAL_CHA_CODE")
    @Expose
    private int chaCode;
    @SerializedName("SAL_LOC_CODE")
    @Expose
    private int locCode;
    @SerializedName("VISITED")
    @Expose
    private int visited;
    @SerializedName("SAL_OWNER")
    @Expose
    private String owner;
    @SerializedName("SAL_LAST_VISIT")
    @Expose
    private String lastVisit;
    @SerializedName("SAL_USE_COMMENT")
    @Expose
    private String userComment;
    @SerializedName("SAL_PHOTO")
    @Expose
    private String photo;
    @SerializedName("SAL_PHONE")
    @Expose
    private String phone;
    @SerializedName("SAL_LATITUDE")
    @Expose
    private String latitude;
    @SerializedName("SAL_LONGITUDE")
    @Expose
    private String longitude;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public int getChaCode() {
        return chaCode;
    }

    public void setChaCode(int chaCode) {
        this.chaCode = chaCode;
    }

    public int getLocCode() {
        return locCode;
    }

    public void setLocCode(int locCode) {
        this.locCode = locCode;
    }

    public int getVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
}
