package kz.smrtx.techmerch.items.entities;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private int saleId;
    @Expose
    @SerializedName("VIS_CREATED")
    private Date created;
    @Expose
    @SerializedName("VIS_START_DATE")
    private Date start;
    @Expose
    @SerializedName("VIS_FINISH_DATE")
    private Date finish;
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

    public int getSaleId() {
        return saleId;
    }

    public Date getCreated() {
        return created;
    }

    public Date getStart() {
        return start;
    }

    public Date getFinish() {
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
}
