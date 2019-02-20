package com.elpoco.p_mapfinder;

public class ShareItem {

    private String title;
    private String text;
    private String filePath;

    public ShareItem(String title, String text, String filePath) {
        this.title = title;
        this.text = text;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
