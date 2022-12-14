package kz.smrtx.techmerch.items.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_REQUEST")
public class Request {
    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("REQ_CODE")
    private String REQ_CODE;
    @Expose
    @SerializedName("REQ_SAL_CODE")
    private int REQ_SAL_CODE;
    @Expose
    @SerializedName("REQ_SAL_NAME")
    private String REQ_SAL_NAME;
    @Expose
    @SerializedName("REQ_CREATED")
    private String REQ_CREATED;
    @Expose
    @SerializedName("REQ_DEADLINE")
    private String REQ_DEADLINE;
    @Expose
    @SerializedName("REQ_TYPE")
    private String REQ_TYPE;
    @Expose
    @SerializedName("REQ_EQUIPMENT")
    private String REQ_EQUIPMENT;
    @Expose
    @SerializedName("REQ_EQU_SUBTYPE")
    private String REQ_EQU_SUBTYPE;
    @Expose
    @SerializedName("REQ_WORK")
    private String REQ_WORK;
    @Expose
    @SerializedName("REQ_REPLACE")
    private String REQ_REPLACE;
    @Expose
    @SerializedName("REQ_REPLACE_ID")
    private int REQ_REPLACE_ID;
    @Expose
    @SerializedName("REQ_ADDITIONAL")
    private String REQ_ADDITIONAL;
    @Expose
    @SerializedName("REQ_SECONDARY_ADDRESS_ID")
    private int REQ_SECONDARY_ADDRESS_ID;
    @Expose
    @SerializedName("REQ_SECONDARY_ADDRESS")
    private String REQ_SECONDARY_ADDRESS;
    @Expose
    @SerializedName("REQ_WORK_SUBTYPE")
    private String REQ_WORK_SUBTYPE;
    @Expose
    @SerializedName("REQ_WORK_SPECIAL")
    private String REQ_WORK_SPECIAL;
    @Expose
    @SerializedName("REQ_COMMENT")
    private String REQ_COMMENT;
    @Expose
    @SerializedName("REQ_STA_ID")
    private int REQ_STA_ID;
    @Expose
    @SerializedName("REQ_HISTORY_CODE")
    private String REQ_HISTORY_CODE;
    @Expose
    @SerializedName("REQ_USE_CODE")
    private int REQ_USE_CODE;
    @Expose
    @SerializedName("REQ_USE_CODE_APPOINTED")
    private int REQ_USE_CODE_APPOINTED;
    @Expose
    @SerializedName("REQ_STATUS")
    private String REQ_STATUS;
    @Expose
    @SerializedName("REQ_USE_NAME")
    private String REQ_USE_NAME;
    @Expose
    @SerializedName("REQ_USE_NAME_APPOINTED")
    private String REQ_USE_NAME_APPOINTED;
    @Expose
    @SerializedName("REQ_USE_PHONE")
    private String REQ_USE_PHONE;
    @Expose
    @SerializedName("REQ_UPDATED")
    private String REQ_UPDATED;
    @Expose
    @SerializedName("REQ_VIS_NUMBER")
    private String REQ_VIS_NUMBER;
    @Expose
    @SerializedName("NES_TO_UPDATE")
    private String NES_TO_UPDATE;

    @NonNull
    public String getREQ_CODE() {
        return REQ_CODE;
    }

    public void setREQ_CODE(@NonNull String REQ_CODE) {
        this.REQ_CODE = REQ_CODE;
    }

    public int getREQ_SAL_CODE() {
        return REQ_SAL_CODE;
    }

    public void setREQ_SAL_CODE(int REQ_SAL_CODE) {
        this.REQ_SAL_CODE = REQ_SAL_CODE;
    }

    public String getREQ_SAL_NAME() {
        return REQ_SAL_NAME;
    }

    public void setREQ_SAL_NAME(String REQ_SAL_NAME) {
        this.REQ_SAL_NAME = REQ_SAL_NAME;
    }

    public String getREQ_CREATED() {
        return REQ_CREATED;
    }

    public void setREQ_CREATED(String REQ_CREATED) {
        this.REQ_CREATED = REQ_CREATED;
    }

    public String getREQ_DEADLINE() {
        return REQ_DEADLINE;
    }

    public void setREQ_DEADLINE(String REQ_DEADLINE) {
        this.REQ_DEADLINE = REQ_DEADLINE;
    }

    public String getREQ_TYPE() {
        return REQ_TYPE;
    }

    public void setREQ_TYPE(String REQ_TYPE) {
        this.REQ_TYPE = REQ_TYPE;
    }

    public String getREQ_EQUIPMENT() {
        return REQ_EQUIPMENT;
    }

    public void setREQ_EQUIPMENT(String REQ_EQUIPMENT) {
        this.REQ_EQUIPMENT = REQ_EQUIPMENT;
    }

    public String getREQ_EQU_SUBTYPE() {
        return REQ_EQU_SUBTYPE;
    }

    public void setREQ_EQU_SUBTYPE(String REQ_EQU_SUBTYPE) {
        this.REQ_EQU_SUBTYPE = REQ_EQU_SUBTYPE;
    }

    public String getREQ_WORK() {
        return REQ_WORK;
    }

    public void setREQ_WORK(String REQ_WORK) {
        this.REQ_WORK = REQ_WORK;
    }

    public String getREQ_REPLACE() {
        return REQ_REPLACE;
    }

    public void setREQ_REPLACE(String REQ_REPLACE) {
        this.REQ_REPLACE = REQ_REPLACE;
    }

    public String getREQ_ADDITIONAL() {
        return REQ_ADDITIONAL;
    }

    public void setREQ_ADDITIONAL(String REQ_ADDITIONAL) {
        this.REQ_ADDITIONAL = REQ_ADDITIONAL;
    }

    public int getREQ_REPLACE_ID() {
        return REQ_REPLACE_ID;
    }

    public void setREQ_REPLACE_ID(int REQ_REPLACE_ID) {
        this.REQ_REPLACE_ID = REQ_REPLACE_ID;
    }

    public int getREQ_SECONDARY_ADDRESS_ID() {
        return REQ_SECONDARY_ADDRESS_ID;
    }

    public void setREQ_SECONDARY_ADDRESS_ID(int REQ_SECONDARY_ADDRESS_ID) {
        this.REQ_SECONDARY_ADDRESS_ID = REQ_SECONDARY_ADDRESS_ID;
    }

    public String getREQ_SECONDARY_ADDRESS() {
        return REQ_SECONDARY_ADDRESS;
    }

    public void setREQ_SECONDARY_ADDRESS(String REQ_SECONDARY_ADDRESS) {
        this.REQ_SECONDARY_ADDRESS = REQ_SECONDARY_ADDRESS;
    }

    public String getREQ_WORK_SUBTYPE() {
        return REQ_WORK_SUBTYPE;
    }

    public void setREQ_WORK_SUBTYPE(String REQ_WORK_SUBTYPE) {
        this.REQ_WORK_SUBTYPE = REQ_WORK_SUBTYPE;
    }

    public String getREQ_WORK_SPECIAL() {
        return REQ_WORK_SPECIAL;
    }

    public void setREQ_WORK_SPECIAL(String REQ_WORK_SPECIAL) {
        this.REQ_WORK_SPECIAL = REQ_WORK_SPECIAL;
    }

    public String getREQ_COMMENT() {
        return REQ_COMMENT;
    }

    public void setREQ_COMMENT(String REQ_COMMENT) {
        this.REQ_COMMENT = REQ_COMMENT;
    }

    public int getREQ_STA_ID() {
        return REQ_STA_ID;
    }

    public void setREQ_STA_ID(int REQ_STA_ID) {
        this.REQ_STA_ID = REQ_STA_ID;
    }

    public String getREQ_HISTORY_CODE() {
        return REQ_HISTORY_CODE;
    }

    public void setREQ_HISTORY_CODE(String REQ_HISTORY_CODE) {
        this.REQ_HISTORY_CODE = REQ_HISTORY_CODE;
    }

    public int getREQ_USE_CODE() {
        return REQ_USE_CODE;
    }

    public void setREQ_USE_CODE(int REQ_USE_CODE) {
        this.REQ_USE_CODE = REQ_USE_CODE;
    }

    public int getREQ_USE_CODE_APPOINTED() {
        return REQ_USE_CODE_APPOINTED;
    }

    public void setREQ_USE_CODE_APPOINTED(int REQ_USE_CODE_APPOINTED) {
        this.REQ_USE_CODE_APPOINTED = REQ_USE_CODE_APPOINTED;
    }

    public String getREQ_STATUS() {
        return REQ_STATUS;
    }

    public void setREQ_STATUS(String REQ_STATUS) {
        this.REQ_STATUS = REQ_STATUS;
    }

    public String getREQ_USE_NAME() {
        return REQ_USE_NAME;
    }

    public void setREQ_USE_NAME(String REQ_USE_NAME) {
        this.REQ_USE_NAME = REQ_USE_NAME;
    }

    public String getREQ_USE_NAME_APPOINTED() {
        return REQ_USE_NAME_APPOINTED;
    }

    public void setREQ_USE_NAME_APPOINTED(String REQ_USE_NAME_APPOINTED) {
        this.REQ_USE_NAME_APPOINTED = REQ_USE_NAME_APPOINTED;
    }

    public String getREQ_UPDATED() {
        return REQ_UPDATED;
    }

    public void setREQ_UPDATED(String REQ_UPDATED) {
        this.REQ_UPDATED = REQ_UPDATED;
    }

    public String getREQ_VIS_NUMBER() {
        return REQ_VIS_NUMBER;
    }

    public void setREQ_VIS_NUMBER(String REQ_VIS_NUMBER) {
        this.REQ_VIS_NUMBER = REQ_VIS_NUMBER;
    }

    public String getNES_TO_UPDATE() {
        return NES_TO_UPDATE;
    }

    public void setNES_TO_UPDATE(String NES_TO_UPDATE) {
        this.NES_TO_UPDATE = NES_TO_UPDATE;
    }

    public String getREQ_USE_PHONE() {
        return REQ_USE_PHONE;
    }

    public void setREQ_USE_PHONE(String REQ_USE_PHONE) {
        this.REQ_USE_PHONE = REQ_USE_PHONE;
    }
}
