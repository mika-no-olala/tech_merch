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
    private int code;
    @Expose
    @SerializedName("VIS_PARENT_CODE")
    private int parentCode;
    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("VIS_NUMBER")
    private String number;
    @Expose
    @SerializedName("VIS_PARENT_NUMBER")
    private String parentNumber;
    @Expose
    @SerializedName("VIS_USE_CODE")
    private int userCode;
    @Expose
    @SerializedName("VIS_VIT_CODE")
    private int vitCode;
    @Expose
    @SerializedName("VIS_VIT_ID")
    private int vitId;
    @Expose
    @SerializedName("VIS_SAL_CODE")
    private int saleCode;
    @Expose
    @SerializedName("VIS_SAL_ID")
    private String saleId;
    @Expose
    @SerializedName("VIS_CREATED")
    private String created;
    @Expose
    @SerializedName("VIS_START_DATE")
    private String start;
    @Expose
    @SerializedName("VIS_FINISH_DATE")
    private String finish;
    @Expose
    @SerializedName("VIS_LONGITUDE")
    private String longitude;
    @Expose
    @SerializedName("VIS_LATITUDE")
    private String latitude;
    @Expose
    @SerializedName("VIS_SYN_ID")
    private String syncId;
    @Expose
    @SerializedName("VIS_DEVICE_ID")
    private String deviceId;
    @Expose
    @SerializedName("VIS_APP_VERSION")
    private String appVersion;
    @Expose
    @SerializedName("VIS_FUS_CODE")
    private int fusCode;
    @Expose
    @SerializedName("VIS_LEA_CODE")
    private int leaCode;

    public int getCode() {
        return code;
    }

    public int getParentCode() {
        return parentCode;
    }

    public String getNumber() {
        return number;
    }

    public String getParentNumber() {
        return parentNumber;
    }

    public int getUserCode() {
        return userCode;
    }

    public int getVitCode() {
        return vitCode;
    }

    public int getVitId() {
        return vitId;
    }

    public int getSaleCode() {
        return saleCode;
    }

    public String getSaleId() {
        return saleId;
    }

    public String getCreated() {
        return created;
    }

    public String getStart() {
        return start;
    }

    public String getFinish() {
        return finish;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getSyncId() {
        return syncId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getFusCode() {
        return fusCode;
    }

    public int getLeaCode() {
        return leaCode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public void setVitCode(int vitCode) {
        this.vitCode = vitCode;
    }

    public void setVitId(int vitId) {
        this.vitId = vitId;
    }

    public void setSaleCode(int saleCode) {
        this.saleCode = saleCode;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setFusCode(int fusCode) {
        this.fusCode = fusCode;
    }

    public void setLeaCode(int leaCode) {
        this.leaCode = leaCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
