package kz.smrtx.techmerch.items;

import androidx.fragment.app.Fragment;

public class RequestPages {
    private int id;
    private String name;
    private int percentage;
    private boolean active;

    public RequestPages(int id, String name, int percentage, boolean active) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
