package kz.smrtx.techmerch.items.entities;
import androidx.room.Entity;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(tableName = "ST_REQUEST")
public class Request {
    @SerializedName("REQ_ID")
    private String id;
    @SerializedName("REQ_CREATED")
    private Date created;
    @SerializedName("REQ_DEADLINE")
    private Date deadline;
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
    @SerializedName("REQ_STATUS")
    private String status;
    @SerializedName("REQ_USE_RESPONSIBLE")
    private String responsible;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
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
}
