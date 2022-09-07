package kz.smrtx.techmerch.items.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.items.entities.User;

public class JsonResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private final List<User> dataList = new ArrayList<>();

    public List<User> getDataList() {
        return dataList;
    }

    public String getMessage() {
        return message;
    }
}
