package com.burdahackday.model;

/**
 * Created by sachinbakshi on 07/10/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineModel implements Parcelable {

    private String mImageUrl;
    private String mDate;
    private String mProgress;

    public TimeLineModel() {
    }

    public TimeLineModel(String mMessage, String mDate, String mProgress) {
        this.mImageUrl = mMessage;
        this.mDate = mDate;
        this.mProgress = mProgress;
    }

    public String getMessage() {
        return mImageUrl;
    }

    public void semMessage(String message) {
        this.mImageUrl = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }


    public String getmProgress() {
        return mProgress;
    }

    public void setmProgress(String mProgress) {
        this.mProgress = mProgress;
    }

   /* public OrderStatus getStatus() {
        return mStatus;
    }

    public void setStatus(OrderStatus mStatus) {
        this.mStatus = mStatus;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mDate);
        //dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
        dest.writeString(this.mProgress);

    }

    protected TimeLineModel(Parcel in) {
        this.mImageUrl = in.readString();
        this.mDate = in.readString();
        this.mProgress = in.readString();

        /*int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];*/
    }

    public static final Parcelable.Creator<TimeLineModel> CREATOR = new Parcelable.Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };
}