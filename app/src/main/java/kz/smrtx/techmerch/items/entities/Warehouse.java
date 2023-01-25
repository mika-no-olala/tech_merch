package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_WAREHOUSE_CONTENT")
public class Warehouse {
    @PrimaryKey
    @NonNull
    @SerializedName("WAC_ID")
    private int WAC_ID;
    @SerializedName("WAC_CONTENT_NAME")
    private String WAC_CONTENT_NAME;
    @SerializedName("WAC_CONTENT_ID")
    private int WAC_CONTENT_ID;
    @SerializedName("WAC_QUANTITY")
    private int WAC_QUANTITY;
    @SerializedName("WAC_WAR_ID")
    private int WAC_WAR_ID;
    @SerializedName("WAC_WAR_NAME")
    private String WAC_WAR_NAME;
    @SerializedName("WAC_LOC_CODE")
    private int WAC_LOC_CODE;
    @SerializedName("WAC_LOC_NAME")
    private String WAC_LOC_NAME;
    @SerializedName("WAC_WAR_ADDRESS")
    private String WAC_WAR_ADDRESS;

    private int WAC_CHANGES;
    private String WAC_USE_CODE;

    @SerializedName("NES_TO_UPDATE")
    private String NES_TO_UPDATE;

    public int getWAC_ID() {
        return WAC_ID;
    }

    public void setWAC_ID(int WAC_ID) {
        this.WAC_ID = WAC_ID;
    }

    public String getWAC_CONTENT_NAME() {
        return WAC_CONTENT_NAME;
    }

    public void setWAC_CONTENT_NAME(String WAC_CONTENT_NAME) {
        this.WAC_CONTENT_NAME = WAC_CONTENT_NAME;
    }

    public int getWAC_CONTENT_ID() {
        return WAC_CONTENT_ID;
    }

    public void setWAC_CONTENT_ID(int WAC_CONTENT_ID) {
        this.WAC_CONTENT_ID = WAC_CONTENT_ID;
    }

    public int getWAC_QUANTITY() {
        return WAC_QUANTITY;
    }

    public void setWAC_QUANTITY(int WAC_QUANTITY) {
        this.WAC_QUANTITY = WAC_QUANTITY;
    }

    public int getWAC_WAR_ID() {
        return WAC_WAR_ID;
    }

    public void setWAC_WAR_ID(int WAC_WAR_ID) {
        this.WAC_WAR_ID = WAC_WAR_ID;
    }

    public String getWAC_WAR_NAME() {
        return WAC_WAR_NAME;
    }

    public void setWAC_WAR_NAME(String WAC_WAR_NAME) {
        this.WAC_WAR_NAME = WAC_WAR_NAME;
    }

    public int getWAC_LOC_CODE() {
        return WAC_LOC_CODE;
    }

    public void setWAC_LOC_CODE(int WAC_LOC_CODE) {
        this.WAC_LOC_CODE = WAC_LOC_CODE;
    }

    public String getWAC_LOC_NAME() {
        return WAC_LOC_NAME;
    }

    public void setWAC_LOC_NAME(String WAC_LOC_NAME) {
        this.WAC_LOC_NAME = WAC_LOC_NAME;
    }

    public String getWAC_WAR_ADDRESS() {
        return WAC_WAR_ADDRESS;
    }

    public void setWAC_WAR_ADDRESS(String WAC_WAR_ADDRESS) {
        this.WAC_WAR_ADDRESS = WAC_WAR_ADDRESS;
    }

    public int getWAC_CHANGES() {
        return WAC_CHANGES;
    }

    public void setWAC_CHANGES(int WAC_CHANGES) {
        this.WAC_CHANGES = WAC_CHANGES;
    }

    public String getWAC_USE_CODE() {
        return WAC_USE_CODE;
    }

    public void setWAC_USE_CODE(String WAC_USE_CODE) {
        this.WAC_USE_CODE = WAC_USE_CODE;
    }

    public String getNES_TO_UPDATE() {
        return NES_TO_UPDATE;
    }

    public void setNES_TO_UPDATE(String NES_TO_UPDATE) {
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }
}
