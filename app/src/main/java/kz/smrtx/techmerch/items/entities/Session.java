package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_SESSION")
public class Session {
    @PrimaryKey
    @NonNull
    @SerializedName("SES_CODE")
    private String SES_CODE;
    @SerializedName("SES_STARTED")
    private String SES_STARTED;
    @SerializedName("SES_FINISHED")
    private String SES_FINISHED;
    @SerializedName("SES_USE_ID")
    private int SES_USE_ID;
    @SerializedName("SES_APP_VERSION")
    private String SES_APP_VERSION;
    @SerializedName("SES_SYNC_ID")
    private String SES_SYNC_ID;

    @NonNull
    public String getSES_CODE() {
        return SES_CODE;
    }

    public void setSES_CODE(@NonNull String SES_CODE) {
        this.SES_CODE = SES_CODE;
    }

    public String getSES_STARTED() {
        return SES_STARTED;
    }

    public void setSES_STARTED(String SES_STARTED) {
        this.SES_STARTED = SES_STARTED;
    }

    public String getSES_FINISHED() {
        return SES_FINISHED;
    }

    public void setSES_FINISHED(String SES_FINISHED) {
        this.SES_FINISHED = SES_FINISHED;
    }

    public int getSES_USE_ID() {
        return SES_USE_ID;
    }

    public void setSES_USE_ID(int SES_USE_ID) {
        this.SES_USE_ID = SES_USE_ID;
    }

    public String getSES_APP_VERSION() {
        return SES_APP_VERSION;
    }

    public void setSES_APP_VERSION(String SES_APP_VERSION) {
        this.SES_APP_VERSION = SES_APP_VERSION;
    }

    public String getSES_SYNC_ID() {
        return SES_SYNC_ID;
    }

    public void setSES_SYNC_ID(String SES_SYNC_ID) {
        this.SES_SYNC_ID = SES_SYNC_ID;
    }
}
