package kz.smrtx.techmerch.items.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_USER")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int idid;

    @SerializedName("USE_ID")
    private String id;

    @SerializedName("USE_CODE")
    private int code;

    @SerializedName("USE_NAME")
    private String name;

    @SerializedName("USE_USR_CODE")
    @Expose
    private int roleCode;

    @SerializedName("USE_USR_NAME")
    private String roleName;

    @SerializedName("USE_LOC_CODE")
    private int locationCode;

    @SerializedName("USE_LOC_NAME")
    private String cityName;

    @SerializedName("USE_VERSION_CODE")
    private int versionCode;

    public User(int code, String name, int roleCode, String roleName, int locationCode, String cityName, int versionCode) {
        this.code = code;
        this.name = name;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.locationCode = locationCode;
        this.cityName = cityName;
        this.versionCode = versionCode;
    }

    public int getIdid() {
        return idid;
    }

    public void setIdid(int idid) {
        this.idid = idid;
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

    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", roleCode=" + roleCode +
                ", roleName='" + roleName + '\'' +
                ", locationCode=" + locationCode +
                ", cityName='" + cityName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
