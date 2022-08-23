package kz.smrtx.techmerch.items;

public class Outlet {
    private String OUT_CODE;
    private String OUT_NAME;
    private String OUT_ADDRESS;
    private int OUT_REQUEST_NUMBER;
    private float OUT_DISTANCE;

    public Outlet(String OUT_CODE, String OUT_NAME, String OUT_ADDRESS, int OUT_REQUEST_NUMBER, float OUT_DISTANCE) {
        this.OUT_CODE = OUT_CODE;
        this.OUT_NAME = OUT_NAME;
        this.OUT_ADDRESS = OUT_ADDRESS;
        this.OUT_REQUEST_NUMBER = OUT_REQUEST_NUMBER;
        this.OUT_DISTANCE = OUT_DISTANCE;
    }

    public String getOUT_CODE() {
        return OUT_CODE;
    }

    public void setOUT_CODE(String OUT_CODE) {
        this.OUT_CODE = OUT_CODE;
    }

    public String getOUT_NAME() {
        return OUT_NAME;
    }

    public void setOUT_NAME(String OUT_NAME) {
        this.OUT_NAME = OUT_NAME;
    }

    public String getOUT_ADDRESS() {
        return OUT_ADDRESS;
    }

    public void setOUT_ADDRESS(String OUT_ADDRESS) {
        this.OUT_ADDRESS = OUT_ADDRESS;
    }

    public int getOUT_REQUEST_NUMBER() {
        return OUT_REQUEST_NUMBER;
    }

    public void setOUT_REQUEST_NUMBER(int OUT_REQUEST_NUMBER) {
        this.OUT_REQUEST_NUMBER = OUT_REQUEST_NUMBER;
    }

    public float getOUT_DISTANCE() {
        return OUT_DISTANCE;
    }

    public void setOUT_DISTANCE(float OUT_DISTANCE) {
        this.OUT_DISTANCE = OUT_DISTANCE;
    }
}
