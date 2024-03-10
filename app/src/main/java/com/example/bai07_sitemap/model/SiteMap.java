package com.example.bai07_sitemap.model;

import java.time.LocalDateTime;
import java.util.List;

public class SiteMap {
    private String url;
    private byte[] image;
    private float prority;
    private String changeFrequency;
    private String lastChange;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public float setPrority() {
        return prority;
    }

    public void setPrority(float prority) {
        this.prority = prority;
    }

    public String getChangeFrequency() {
        return changeFrequency;
    }

    public void setChangeFrequency(String changeFrequency) {
        this.changeFrequency = changeFrequency;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }
}
