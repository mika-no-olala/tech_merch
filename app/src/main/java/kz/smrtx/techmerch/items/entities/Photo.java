package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_REQUEST_PHOTO")
public class Photo {
    @PrimaryKey
    @NonNull
    @SerializedName("REP_CODE")
    private String REP_CODE;
    @SerializedName("REP_REQ_CODE")
    private String REP_REQ_CODE;
    @SerializedName("REP_PHOTO")
    private String REP_PHOTO;
    @SerializedName("REP_USE_CODE")
    private String REP_USE_CODE;
    @SerializedName("REP_USE_ROLE")
    private int REP_USE_ROLE;
    @SerializedName("NES_TO_UPDATE")
    private String NES_TO_UPDATE;

    public Photo(@NonNull String REP_CODE, String REP_REQ_CODE, String REP_PHOTO, String REP_USE_CODE, int REP_USE_ROLE, String NES_TO_UPDATE) {
        this.REP_CODE = REP_CODE;
        this.REP_REQ_CODE = REP_REQ_CODE;
        this.REP_PHOTO = REP_PHOTO;
        this.REP_USE_CODE = REP_USE_CODE;
        this.REP_USE_ROLE = REP_USE_ROLE;
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "REP_PHOTO='" + REP_PHOTO + '\'' +
                '}';
    }

    public String getNES_TO_UPDATE() {
        return NES_TO_UPDATE;
    }

    public void setNES_TO_UPDATE(String NES_TO_UPDATE) {
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }

    public String getREP_CODE() {
        return REP_CODE;
    }

    public void setREP_CODE(String REP_CODE) {
        this.REP_CODE = REP_CODE;
    }

    public String getREP_REQ_CODE() {
        return REP_REQ_CODE;
    }

    public void setREP_REQ_CODE(String REP_REQ_CODE) {
        this.REP_REQ_CODE = REP_REQ_CODE;
    }

    public String getREP_PHOTO() {
        return REP_PHOTO;
    }

    public void setREP_PHOTO(String REP_PHOTO) {
        this.REP_PHOTO = REP_PHOTO;
    }

    public String getREP_USE_CODE() {
        return REP_USE_CODE;
    }

    public void setREP_USE_CODE(String REP_USE_CODE) {
        this.REP_USE_CODE = REP_USE_CODE;
    }

    public int getREP_USE_ROLE() {
        return REP_USE_ROLE;
    }

    public void setREP_USE_ROLE(int REP_USE_ROLE) {
        this.REP_USE_ROLE = REP_USE_ROLE;
    }
}
