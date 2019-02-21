package com.elpoco.p_mapfinder;

public class CommentItem {
    private String id,comment, boardNum;

    public CommentItem(String id, String comment,String num) {
        this.id = id;
        this.comment = comment;
        this.boardNum =num;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getBoardNum() {
        return boardNum;
    }
}
