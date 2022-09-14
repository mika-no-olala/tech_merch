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
    private String code;
    @SerializedName("SES_STARTED")
    private String started;
    @SerializedName("SES_FINISHED")
    private String finished;
    @SerializedName("SES_USE_ID")
    private int userId;
    @SerializedName("SES_SYNC_ID")
    private String syncId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSyncId() {
        return syncId;
    }

    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }
}
