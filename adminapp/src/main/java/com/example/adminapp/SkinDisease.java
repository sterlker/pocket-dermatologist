package com.example.adminapp;

public class SkinDisease {
    private String title_sd;
    private String expand_sd;

    public SkinDisease(String title_sd, String expand_sd) {
        this.title_sd = title_sd;
        this.expand_sd = expand_sd;
    }

    public String getTitle_sd() {
        return title_sd;
    }

    public void setTitle_sd(String title_sd) {
        this.title_sd = title_sd;
    }

    public String getExpand_sd() {
        return expand_sd;
    }

    public void setExpand_sd(String expand_sd) {
        this.expand_sd = expand_sd;
    }
}
