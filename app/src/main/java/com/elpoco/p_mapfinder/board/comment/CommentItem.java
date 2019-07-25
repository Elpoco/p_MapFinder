package com.elpoco.p_mapfinder.board.comment;

public class CommentItem {
    private String nickName;
    private String comment;
    private String profileUrl;

    public CommentItem() {
    }

    public CommentItem(String id, String comment, String profileUrl) {
        this.nickName = id;
        this.comment = comment;
        this.profileUrl = profileUrl;
    }


    public String getId() {
        return nickName;
    }

    public String getComment() {
        return comment;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
