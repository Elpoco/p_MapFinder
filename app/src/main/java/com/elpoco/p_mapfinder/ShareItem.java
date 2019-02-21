package com.elpoco.p_mapfinder;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareItem implements Parcelable {

    private String title;
    private String text;
    private String filePath;

    public ShareItem(String title, String text, String filePath) {
        this.title = title;
        this.text = text;
        this.filePath = filePath;
    }

    protected ShareItem(Parcel in) {
        title = in.readString();
        text = in.readString();
        filePath = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(filePath);
    }
}
