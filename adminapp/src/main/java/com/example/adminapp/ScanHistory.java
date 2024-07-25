package com.example.adminapp;

public class ScanHistory {
    private String image_sh;
    private String title_sh;
    private String expand_sh;

    public ScanHistory(String image_sh, String title_sh, String expand_sh) {
        this.image_sh = image_sh;
        this.title_sh = title_sh;
        this.expand_sh = expand_sh;
    }

    public String getImage_sh() {
        return image_sh;
    }

    public void setImage_sh(String image_sh) {
        this.image_sh = image_sh;
    }

    public String getTitle_sh() {
        return title_sh;
    }

    public void setTitle_sh(String title_sh) {
        this.title_sh = title_sh;
    }

    public String getExpand_sh() {
        return expand_sh;
    }

    public void setExpand_sh(String expand_sh) {
        this.expand_sh = expand_sh;
    }
}
