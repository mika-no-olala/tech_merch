package kz.smrtx.techmerch.items.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "WAREHOUSE_JOURNAL")
public class WarehouseJournal {
    @PrimaryKey
    @SerializedName("WAJ_ID")
    private int id;
    @SerializedName("WAR_NAME_SOURCE")
    private String warehouseNameSource;
    @SerializedName("WAR_NAME")
    private String warehouseName;
    @SerializedName("WAC_CONTENT_NAME")
    private String content;
    @SerializedName("WAJ_DATE")
    private String date;
    @SerializedName("WAJ_CHANGE")
    private int quantity;

    public WarehouseJournal(int id, String warehouseNameSource, String warehouseName, String content, String date, int quantity) {
        this.id = id;
        this.warehouseNameSource = warehouseNameSource;
        this.warehouseName = warehouseName;
        this.content = content;
        this.date = date;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarehouseNameSource() {
        return warehouseNameSource;
    }

    public void setWarehouseNameSource(String warehouseNameSource) {
        this.warehouseNameSource = warehouseNameSource;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
