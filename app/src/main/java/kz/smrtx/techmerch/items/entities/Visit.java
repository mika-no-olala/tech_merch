package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.type.DateTime;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "ST_VISIT")
public class Visit implements Serializable {
    @Expose
    @SerializedName("VIS_CODE")
    private int VIS_CODE;
    @Expose
    @SerializedName("VIS_PARENT_CODE")
    private int VIS_PARENT_CODE;
    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("VIS_NUMBER")
    private String VIS_NUMBER;
    @Expose
    @SerializedName("VIS_PARENT_NUMBER")
    private String VIS_PARENT_NUMBER;
    @Expose
    @SerializedName("VIS_USE_CODE")
    private int VIS_USE_CODE;
    @Expose
    @SerializedName("VIS_VIT_CODE")
    private int VIS_VIT_CODE;
    @Expose
    @SerializedName("VIS_VIT_ID")
    private int VIS_VIT_ID;
    @Expose
    @SerializedName("VIS_SAL_CODE")
    private int VIS_SAL_CODE;
    @Expose
    @SerializedName("VIS_SAL_ID")
    private String VIS_SAL_ID;
    @Expose
    @SerializedName("VIS_CREATED")
    private String VIS_CREATED;
    @Expose
    @SerializedName("VIS_START_DATE")
    private String VIS_START_DATE;
    @Expose
    @SerializedName("VIS_FINISH_DATE")
    private String VIS_FINISH_DATE;
    @Expose
    @SerializedName("VIS_LONGITUDE")
    private String VIS_LONGITUDE;
    @Expose
    @SerializedName("VIS_LATITUDE")
    private String VIS_LATITUDE;
    @Expose
    @SerializedName("VIS_SYN_ID")
    private String VIS_SYN_ID;
    @Expose
    @SerializedName("VIS_DEVICE_ID")
    private String VIS_DEVICE_ID;
    @Expose
    @SerializedName("VIS_SES_CODE")
    private String VIS_SES_CODE;

    public int getVIS_CODE() {
        return VIS_CODE;
    }

    public void setVIS_CODE(int VIS_CODE) {
        this.VIS_CODE = VIS_CODE;
    }

    public int getVIS_PARENT_CODE() {
        return VIS_PARENT_CODE;
    }

    public void setVIS_PARENT_CODE(int VIS_PARENT_CODE) {
        this.VIS_PARENT_CODE = VIS_PARENT_CODE;
    }

    @NonNull
    public String getVIS_NUMBER() {
        return VIS_NUMBER;
    }

    public void setVIS_NUMBER(@NonNull String VIS_NUMBER) {
        this.VIS_NUMBER = VIS_NUMBER;
    }

    public String getVIS_PARENT_NUMBER() {
        return VIS_PARENT_NUMBER;
    }

    public void setVIS_PARENT_NUMBER(String VIS_PARENT_NUMBER) {
        this.VIS_PARENT_NUMBER = VIS_PARENT_NUMBER;
    }

    public int getVIS_USE_CODE() {
        return VIS_USE_CODE;
    }

    public void setVIS_USE_CODE(int VIS_USE_CODE) {
        this.VIS_USE_CODE = VIS_USE_CODE;
    }

    public int getVIS_VIT_CODE() {
        return VIS_VIT_CODE;
    }

    public void setVIS_VIT_CODE(int VIS_VIT_CODE) {
        this.VIS_VIT_CODE = VIS_VIT_CODE;
    }

    public int getVIS_VIT_ID() {
        return VIS_VIT_ID;
    }

    public void setVIS_VIT_ID(int VIS_VIT_ID) {
        this.VIS_VIT_ID = VIS_VIT_ID;
    }

    public int getVIS_SAL_CODE() {
        return VIS_SAL_CODE;
    }

    public void setVIS_SAL_CODE(int VIS_SAL_CODE) {
        this.VIS_SAL_CODE = VIS_SAL_CODE;
    }

    public String getVIS_SAL_ID() {
        return VIS_SAL_ID;
    }

    public void setVIS_SAL_ID(String VIS_SAL_ID) {
        this.VIS_SAL_ID = VIS_SAL_ID;
    }

    public String getVIS_CREATED() {
        return VIS_CREATED;
    }

    public void setVIS_CREATED(String VIS_CREATED) {
        this.VIS_CREATED = VIS_CREATED;
    }

    public String getVIS_START_DATE() {
        return VIS_START_DATE;
    }

    public void setVIS_START_DATE(String VIS_START_DATE) {
        this.VIS_START_DATE = VIS_START_DATE;
    }

    public String getVIS_FINISH_DATE() {
        return VIS_FINISH_DATE;
    }

    public void setVIS_FINISH_DATE(String VIS_FINISH_DATE) {
        this.VIS_FINISH_DATE = VIS_FINISH_DATE;
    }

    public String getVIS_LONGITUDE() {
        return VIS_LONGITUDE;
    }

    public void setVIS_LONGITUDE(String VIS_LONGITUDE) {
        this.VIS_LONGITUDE = VIS_LONGITUDE;
    }

    public String getVIS_LATITUDE() {
        return VIS_LATITUDE;
    }

    public void setVIS_LATITUDE(String VIS_LATITUDE) {
        this.VIS_LATITUDE = VIS_LATITUDE;
    }

    public String getVIS_SYN_ID() {
        return VIS_SYN_ID;
    }

    public void setVIS_SYN_ID(String VIS_SYN_ID) {
        this.VIS_SYN_ID = VIS_SYN_ID;
    }

    public String getVIS_DEVICE_ID() {
        return VIS_DEVICE_ID;
    }

    public void setVIS_DEVICE_ID(String VIS_DEVICE_ID) {
        this.VIS_DEVICE_ID = VIS_DEVICE_ID;
    }

    public String getVIS_SES_CODE() {
        return VIS_SES_CODE;
    }

    public void setVIS_SES_CODE(String VIS_SES_CODE) {
        this.VIS_SES_CODE = VIS_SES_CODE;
    }
}
