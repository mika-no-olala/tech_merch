package kz.smrtx.techmerch.items.entities;

import com.google.gson.annotations.SerializedName;

public class StringData {
    @SerializedName("rows")
    private int rows;

    public int getRows() {
        return rows;
    }
}
