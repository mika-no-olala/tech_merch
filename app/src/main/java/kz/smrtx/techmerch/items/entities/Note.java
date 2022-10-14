package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_NOTES")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int NOT_ID;
    @SerializedName("NOT_CREATED")
    private String NOT_CREATED;
    @SerializedName("NOT_SAL_CODE")
    private int NOT_SAL_CODE;
    @SerializedName("NOT_USE_CODE")
    private int NOT_USE_CODE;
    @SerializedName("NOT_USE_NAME")
    private String NOT_USE_NAME;
    @SerializedName("NOT_TEXT")
    private String NOT_TEXT;
    @SerializedName("NOT_VIS_NUMBER")
    private String NOT_VIS_NUMBER;
    @SerializedName("NES_TO_UPDATE")
    private String NES_TO_UPDATE;

    public Note(String NOT_CREATED, int NOT_SAL_CODE, int NOT_USE_CODE, String NOT_USE_NAME, String NOT_TEXT, String NOT_VIS_NUMBER, String NES_TO_UPDATE) {
        this.NOT_CREATED = NOT_CREATED;
        this.NOT_SAL_CODE = NOT_SAL_CODE;
        this.NOT_USE_CODE = NOT_USE_CODE;
        this.NOT_USE_NAME = NOT_USE_NAME;
        this.NOT_TEXT = NOT_TEXT;
        this.NOT_VIS_NUMBER = NOT_VIS_NUMBER;
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }

    public int getNOT_ID() {
        return NOT_ID;
    }

    public void setNOT_ID(int NOT_ID) {
        this.NOT_ID = NOT_ID;
    }

    public String getNOT_CREATED() {
        return NOT_CREATED;
    }

    public void setNOT_CREATED(String NOT_CREATED) {
        this.NOT_CREATED = NOT_CREATED;
    }

    public int getNOT_SAL_CODE() {
        return NOT_SAL_CODE;
    }

    public void setNOT_SAL_CODE(int NOT_SAL_CODE) {
        this.NOT_SAL_CODE = NOT_SAL_CODE;
    }

    public int getNOT_USE_CODE() {
        return NOT_USE_CODE;
    }

    public void setNOT_USE_CODE(int NOT_USE_CODE) {
        this.NOT_USE_CODE = NOT_USE_CODE;
    }

    public String getNOT_TEXT() {
        return NOT_TEXT;
    }

    public void setNOT_TEXT(String NOT_TEXT) {
        this.NOT_TEXT = NOT_TEXT;
    }

    public String getNOT_USE_NAME() {
        return NOT_USE_NAME;
    }

    public void setNOT_USE_NAME(String NOT_USE_NAME) {
        this.NOT_USE_NAME = NOT_USE_NAME;
    }

    public String getNOT_VIS_NUMBER() {
        return NOT_VIS_NUMBER;
    }

    public void setNOT_VIS_NUMBER(String NOT_VIS_NUMBER) {
        this.NOT_VIS_NUMBER = NOT_VIS_NUMBER;
    }

    public String getNES_TO_UPDATE() {
        return NES_TO_UPDATE;
    }

    public void setNES_TO_UPDATE(String NES_TO_UPDATE) {
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }
}
