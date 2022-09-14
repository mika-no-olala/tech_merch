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
    private String id;
    @SerializedName("ELE_NAME")
    private String name;
    @SerializedName("ELE_CATEGORY_CODE")
    private String categoryCode;
    @SerializedName("ELE_CATEGORY_NAME")
    private String categoryName;
    @SerializedName("ELE_SUBTYPE_FROM")
    private String subtypeFrom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubtypeFrom() {
        return subtypeFrom;
    }

    public void setSubtypeFrom(String subtypeFrom) {
        this.subtypeFrom = subtypeFrom;
    }
}
