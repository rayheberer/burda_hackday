package com.burdahackday.model;

/**
 * Created by sachinbakshi on 08/10/17.
 */

public class Post {

    public Post(String mImageUrl, String mDate, String mProgress, User mUser) {
        this.mImageUrl = mImageUrl;
        this.mDate = mDate;
        this.mProgress = mProgress;
        this.user = mUser;
    }
    public Post(){

    }


    public String mImageUrl;
    public String mDate;
    public String mProgress;
    public User user;
}
