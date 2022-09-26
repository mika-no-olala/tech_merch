package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_REQUEST_HISTORY")
public class History {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("HIS_REQ_CODE")
    private String requestCode;
    @SerializedName("HIS_REQ_SAL_CODE")
    private int salePointCode;
    @SerializedName("HIS_SAL_NAME")
    private String salePointName;
    @SerializedName("HIS_STA_ID")
    private int statusId;
    @SerializedName("HIS_STA_NAME")
    private String status;
    @SerializedName("HIS_USE_CODE")
    private int userCode;
    @SerializedName("HIS_USE_NAME")
    private String userName;
    @SerializedName("HIS_USR_CODE")
    private int userRole;
    @SerializedName("HIS_USE_CODE_APPOINTED")
    private int userCodeAppointed;
    @SerializedName("HIS_USE_NAME_APPOINTED")
    private String userNameAppointed;
    @SerializedName("HIS_USR_CODE_APPOINTED")
    private int userRoleAppointed;
    @SerializedName("HIS_REQ_COMMENT")
    private String comment;
    @SerializedName("HIS_REQ_CREATED")
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public int getSalePointCode() {
        return salePointCode;
    }

    public void setSalePointCode(int salePointCode) {
        this.salePointCode = salePointCode;
    }

    public String getSalePointName() {
        return salePointName;
    }

    public void setSalePointName(String salePointName) {
        this.salePointName = salePointName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserCodeAppointed() {
        return userCodeAppointed;
    }

    public void setUserCodeAppointed(int userCodeAppointed) {
        this.userCodeAppointed = userCodeAppointed;
    }

    public String getUserNameAppointed() {
        return userNameAppointed;
    }

    public void setUserNameAppointed(String userNameAppointed) {
        this.userNameAppointed = userNameAppointed;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getUserRoleAppointed() {
        return userRoleAppointed;
    }

    public void setUserRoleAppointed(int userRoleAppointed) {
        this.userRoleAppointed = userRoleAppointed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
