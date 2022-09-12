
package kz.smrtx.techmerch.items.reqres.synctables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Table {

    @SerializedName("USE_CODE")
    @Expose
    private Integer uSECODE;

    @SerializedName("MVL_VIEW_NAME")
    @Expose
    private String mVLVIEWNAME;

    @SerializedName("MVL_TABLE_NAME")
    @Expose
    private String mVLTABLENAME;

    @SerializedName("MVL_REFERENCE")
    @Expose
    private Integer mVLREFERENCE;

    public Integer getUSECODE() {
        return uSECODE;
    }

    public void setUSECODE(Integer uSECODE) {
        this.uSECODE = uSECODE;
    }

    public String getMVLVIEWNAME() {
        return mVLVIEWNAME;
    }

    public void setMVLVIEWNAME(String mVLVIEWNAME) {
        this.mVLVIEWNAME = mVLVIEWNAME;
    }

    public String getMVLTABLENAME() {
        return mVLTABLENAME;
    }

    public void setMVLTABLENAME(String mVLTABLENAME) {
        this.mVLTABLENAME = mVLTABLENAME;
    }

    public Integer getMVLREFERENCE() {
        return mVLREFERENCE;
    }

    public void setMVLREFERENCE(Integer mVLREFERENCE) {
        this.mVLREFERENCE = mVLREFERENCE;
    }

}
