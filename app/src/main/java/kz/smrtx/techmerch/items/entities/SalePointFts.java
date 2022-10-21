package kz.smrtx.techmerch.items.entities;

import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = SalePoint.class)
@Entity(tableName = "ST_SALEPOINT_FTS")
public class SalePointFts {

    private int code;

    private String id;

    private String name;

    private String street;

    private String latitude;

    private String longitude;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
