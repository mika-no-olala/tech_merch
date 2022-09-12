package kz.smrtx.techmerch.items.entities;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_USER")
public class User {
    @SerializedName("USE_ID")
    private String id;

    @SerializedName("USE_CODE")
    private int code;

    @SerializedName("USE_NAME")
    private String name;

    @SerializedName("USE_USR_CODE")
    @Expose
    private String roleCode;

    @SerializedName("USR_NAME")
    private String roleName;

    @SerializedName("USE_LOC_CODE")
    private int locationCode;

    @SerializedName("USE_VERSION_CODE")
    private int versionCode;

    public User(String name, String roleCode, String roleName, int locationCode, int versionCode) {
        this.name = name;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.locationCode = locationCode;
        this.versionCode = versionCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
