
package kz.smrtx.techmerch.items.reqres.synctables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncTables {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Table> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Table> getData() {
        return data;
    }

    public void setData(List<Table> data) {
        this.data = data;
    }

}
