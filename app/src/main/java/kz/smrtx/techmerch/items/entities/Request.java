package kz.smrtx.techmerch.items.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ST_REQUEST")
public class Request {
    @SerializedName("REQ_ID")
    private String id;
    @PrimaryKey
    @NonNull
    @SerializedName("REQ_CODE")
    private String code;
    @SerializedName("REQ_SAL_CODE")
    private int salePointCode;
    @SerializedName("REQ_SAL_NAME")
    private String salePointName;
    @SerializedName("REQ_CREATED")
    private String created;
    @SerializedName("REQ_DEADLINE")
    private String deadline;
    @SerializedName("REQ_TYPE")
    private String type;
    @SerializedName("REQ_EQUIPMENT")
    private String equipment;
    @SerializedName("REQ_EQU_SUBTYPE")
    private String equipmentSubtype;
    @SerializedName("REQ_WORK")
    private String work;
    @SerializedName("REQ_REPLACE")
    private String replace;
    @SerializedName("REQ_ADDITIONAL")
    private String additional;
    @SerializedName("REQ_ADDRESS_SALEPOINT")
    private String addressSalePoint;
    @SerializedName("REQ_WORK_SUBTYPE")
    private String workSubtype;
    @SerializedName("REQ_WORK_SPECIAL")
    private String workSpecial;
    @SerializedName("REQ_COMMENT")
    private String comment;
    @SerializedName("REQ_STATUS_ID")
    private int statusId;
    @SerializedName("REQ_USE_RESPONSIBLE_CODE")
    private int responsibleCode;
    @SerializedName("REQ_STATUS")
    private String status;
    @SerializedName("REQ_USE_RESPONSIBLE")
    private String responsible;
    @SerializedName("REQ_VIS_NUMBER")
    private String visitNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentSubtype() {
        return equipmentSubtype;
    }

    public void setEquipmentSubtype(String equipmentSubtype) {
        this.equipmentSubtype = equipmentSubtype;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAddressSalePoint() {
        return addressSalePoint;
    }

    public void setAddressSalePoint(String addressSalePoint) {
        this.addressSalePoint = addressSalePoint;
    }

    public String getWorkSubtype() {
        return workSubtype;
    }

    public void setWorkSubtype(String workSubtype) {
        this.workSubtype = workSubtype;
    }

    public String getWorkSpecial() {
        return workSpecial;
    }

    public void setWorkSpecial(String workSpecial) {
        this.workSpecial = workSpecial;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getResponsibleCode() {
        return responsibleCode;
    }

    public void setResponsibleCode(int responsibleCode) {
        this.responsibleCode = responsibleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }
}
