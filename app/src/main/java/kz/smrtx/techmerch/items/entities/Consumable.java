package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_TECHNIC_REPORT")
public class Consumable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("TER_ID")
    private int TER_ID;
    @SerializedName("TER_CODE")
    private String TER_CODE;
    @SerializedName("TER_CONSUMABLE_NAME")
    private String TER_CONSUMABLE_NAME;
    @SerializedName("TER_CONSUMABLE_ID")
    private int TER_CONSUMABLE_ID;
    @SerializedName("TER_COST")
    private int TER_COST;
    @SerializedName("TER_QUANTITY")
    private int TER_QUANTITY;
    @SerializedName("TER_USE_CODE")
    private int TER_USE_CODE;
    @SerializedName("TER_USE_NAME")
    private String TER_USE_NAME;
    @SerializedName("TER_CREATED")
    private String TER_CREATED;

    public Consumable(String TER_CONSUMABLE_NAME, int TER_CONSUMABLE_ID, int TER_COST, int TER_QUANTITY) {
        this.TER_CONSUMABLE_NAME = TER_CONSUMABLE_NAME;
        this.TER_CONSUMABLE_ID = TER_CONSUMABLE_ID;
        this.TER_COST = TER_COST;
        this.TER_QUANTITY = TER_QUANTITY;
    }

    public int getTER_ID() {
        return TER_ID;
    }

    public void setTER_ID(int TER_ID) {
        this.TER_ID = TER_ID;
    }

    public String getTER_CODE() {
        return TER_CODE;
    }

    public void setTER_CODE(String TER_CODE) {
        this.TER_CODE = TER_CODE;
    }

    public String getTER_CONSUMABLE_NAME() {
        return TER_CONSUMABLE_NAME;
    }

    public void setTER_CONSUMABLE_NAME(String TER_CONSUMABLE_NAME) {
        this.TER_CONSUMABLE_NAME = TER_CONSUMABLE_NAME;
    }

    public int getTER_CONSUMABLE_ID() {
        return TER_CONSUMABLE_ID;
    }

    public void setTER_CONSUMABLE_ID(int TER_CONSUMABLE_ID) {
        this.TER_CONSUMABLE_ID = TER_CONSUMABLE_ID;
    }

    public int getTER_COST() {
        return TER_COST;
    }

    public void setTER_COST(int TER_COST) {
        this.TER_COST = TER_COST;
    }

    public int getTER_QUANTITY() {
        return TER_QUANTITY;
    }

    public void setTER_QUANTITY(int TER_QUANTITY) {
        this.TER_QUANTITY = TER_QUANTITY;
    }

    public int getTER_USE_CODE() {
        return TER_USE_CODE;
    }

    public void setTER_USE_CODE(int TER_USE_CODE) {
        this.TER_USE_CODE = TER_USE_CODE;
    }

    public String getTER_USE_NAME() {
        return TER_USE_NAME;
    }

    public void setTER_USE_NAME(String TER_USE_NAME) {
        this.TER_USE_NAME = TER_USE_NAME;
    }

    public String getTER_CREATED() {
        return TER_CREATED;
    }

    public void setTER_CREATED(String TER_CREATED) {
        this.TER_CREATED = TER_CREATED;
    }
}
