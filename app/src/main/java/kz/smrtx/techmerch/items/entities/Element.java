package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_LIST_ELEMENTS")
public class Element {
    @PrimaryKey
    @NonNull
    @SerializedName("ELE_ID")
    private int id;
    @SerializedName("ELE_NAME")
    private String name;
    @SerializedName("ELE_CATEGORY_CODE")
    private int categoryCode;
    @SerializedName("ELE_CATEGORY_NAME")
    private String categoryName;
    @SerializedName("ELE_SUBTYPE_FROM")
    private int subtypeFrom;

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

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSubtypeFrom() {
        return subtypeFrom;
    }

    public void setSubtypeFrom(int subtypeFrom) {
        this.subtypeFrom = subtypeFrom;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", subtypeFrom=" + subtypeFrom +
                '}';
    }
}
