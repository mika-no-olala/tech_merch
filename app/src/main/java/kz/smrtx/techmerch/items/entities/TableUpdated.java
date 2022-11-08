package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "WT_TABLES_UPDATED")
public class TableUpdated {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("TAU_TABLE_NAME")
    private String name;
    @SerializedName("TAU_LAST_UPDATE")
    private String updated;

    public TableUpdated(String name, String updated) {
        this.name = name;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
