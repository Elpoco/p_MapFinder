package com.elpoco.p_mapfinder;

public class CommentItem {
    private String id,comment;

    public CommentItem(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }
}
