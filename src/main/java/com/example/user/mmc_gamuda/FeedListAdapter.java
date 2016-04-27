package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user on 9/23/15.
 */
public class FeedListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private Context c;
    private Util util;


    public FeedListAdapter(Activity activity, List<FeedItem> feedItems, Context c) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.c = c;
        Util util = new Util();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        ImageView profilePic = (ImageView) convertView
                .findViewById(R.id.profilePic);
        ImageView feedImageView = (ImageView) convertView
                .findViewById(R.id.feedImage1);

        FeedItem item = feedItems.get(position);

        name.setText(item.getName());

        // Converting timestamp into x ago format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time =  sdf.parse(item.getTimeStamp()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }


            // url is null, remove from the view
            url.setVisibility(View.GONE);

        // user profile pic


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("I", color1);

        profilePic.setImageDrawable(drawable);



        // Feed image
        if (item.getImge() != null) {
            feedImageView.setVisibility(View.VISIBLE);
            //Picasso.with(c).load(util.downloadImg(c,item.getImge())).into(feedImageView);
            Picasso.with(c).load(R.drawable.cosmos).into(feedImageView);
            feedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Matrix matrix = img.getDisplayMatrix();
                    img.setImageBitmap( bitmap, matrix );
                    */
                }
            });

        } else {

            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
