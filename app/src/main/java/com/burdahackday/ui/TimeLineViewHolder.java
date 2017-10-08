package com.burdahackday.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burdahackday.ui.R;
import com.github.vipulasri.timelineview.TimelineView;

/**
 * Created by sachinbakshi on 07/10/17.
 */

public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    TextView mDate;
    TextView mMessage;
    ImageView mImage ;
    TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

      mDate = (TextView)itemView.findViewById(R.id.text_timeline_date);
        mMessage = (TextView)itemView.findViewById(R.id.text_timeline_title) ;
        mImage = (ImageView)itemView.findViewById(R.id.image_timeline_title) ;
        mTimelineView = (TimelineView)itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}