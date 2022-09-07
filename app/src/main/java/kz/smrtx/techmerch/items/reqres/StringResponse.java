package kz.smrtx.techmerch.items.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.items.entities.StringData;

public class StringResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private final List<StringData> dataList = new ArrayList<>();

    public List<StringData> getDataList() {
        return dataList;
    }

    public String getMessage() {
        return message;
    }
}
