package com.elpoco.p_mapfinder.board.share;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareItem implements Parcelable {

    private String title;
    private String nickName;
    private String profileUrl;
    private String text;
    private String filePath;
    private String boardNum;
    private String token;

    public ShareItem(String title, String nickName, String profileUrl,  String boardNum,String text, String filePath,String token) {
        this.title = title;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.text = text;
        this.filePath = filePath;
        this.boardNum = boardNum;
        this.token = token;
    }

    public ShareItem() {
    }

    protected ShareItem(Parcel in) {
        title = in.readString();
        nickName=in.readString();
        profileUrl=in.readString();
        text = in.readString();
        filePath = in.readString();
        boardNum = in.readString();
        token = in.readString();
    }

    public static final Creator<ShareItem> CREATOR = new Creator<ShareItem>() {
        @Override
        public ShareItem createFromParcel(Parcel in) {
            return new ShareItem(in);
        }

        @Override
        public ShareItem[] newArray(int size) {
            return new ShareItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getBoardNum() {
        return boardNum;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(nickName);
        dest.writeString(profileUrl);
        dest.writeString(text);
        dest.writeString(filePath);
        dest.writeString(boardNum);
        dest.writeString(token);
    }
}
