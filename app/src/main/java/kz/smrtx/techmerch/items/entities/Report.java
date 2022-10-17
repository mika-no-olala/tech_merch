package kz.smrtx.techmerch.items.entities;

public class Report {
    private String code;
    private String dateInterval;
    private String item;
    private int total;
    private String date;

    public Report(String code, String dateInterval, String item, int total, String date) {
        this.code = code;
        this.dateInterval = dateInterval;
        this.item = item;
        this.total = total;
        this.date = date;
    }

    public Report() {
    }

    public String getCode() {
        return code;
    }

    public String getDateInterval() {
        return dateInterval;
    }

    public String getItem() {
        return item;
    }

    public int getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
