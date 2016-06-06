package com.plenry.sparkline.adapter;

/**
 * Created by Xiaoyu on 5/19/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plenry.sparkline.R;
import com.plenry.sparkline.bean.Message;
import com.plenry.sparkline.data.ImageHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ChatMsgAdapter extends ArrayAdapter<Message> {
    public TextView tvSendTime;
    public TextView tvUserName;
    public TextView tvContent;
    public TextView tvTime;
    public ImageView userImage;
    private List<Message> messageList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public ChatMsgAdapter(Context context, int resource, List<Message> msg) {
        super(context, resource, msg);
        this.messageList = msg;
        layoutInflater = LayoutInflater.from(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg = messageList.get(position);
        if (convertView == null) {
            if (msg.getMsgType()) {
                convertView = layoutInflater.inflate(R.layout.chat_msg_left, null);
            } else {
                convertView = layoutInflater.inflate(R.layout.chat_msg_right, null);
            }
            tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            userImage = (ImageView) convertView.findViewById(R.id.iv_userhead);
        }
        try {
            tvSendTime.setText(timeParser(msg.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        imageLoader.loadImage(messageList.get(position).getPhoto(), options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                userImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(loadedImage, 100));
            }
        });
//        userImage.setImageBitmap();
//        imageLoader.displayImage(messageList.get(position).getPhoto(), userImage, options);
        tvContent.setText(msg.getContent());
        tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvUserName.setText(msg.getUsername());
        return convertView;
    }

    public String timeParser(String time) throws ParseException {
        Date date = new SimpleDateFormat("yyyyMMddHHmmssZ").parse(time);
        return new SimpleDateFormat("MMM dd, HH:mm").format(date);
    }
}