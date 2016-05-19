package com.plenry.sparkline.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plenry.sparkline.R;
import com.plenry.sparkline.bean.Message;
import com.plenry.sparkline.data.DownloadImageTask;

import java.util.List;


/**
 * Created by Xiaoyu on 5/17/16.
 */
public class ChatAdapter extends ArrayAdapter<Message> {
    private final List<Message> messages;
    public  ChatAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        if (convertView == null) {
            // This is an expensive operation! Avoid and reuse as much as possible.
            row = inflater.inflate(R.layout.message_layout, parent, false);
        }

        ImageView leftImage = (ImageView)row.findViewById(R.id.imageIconLeft);

        new DownloadImageTask(leftImage)
                .execute(messages.get(position).getUser().getPhoto());
        //
        //leftImage.setImageResource();

        TextView messageView = (TextView) row.findViewById(R.id.messageTxt);
        messageView.setText(messages.get(position).getContent());
        ImageView rightImage = (ImageView) row.findViewById(R.id.imageIconRight);
        new DownloadImageTask((ImageView) row.findViewById(R.id.imageIconRight))
                .execute(messages.get(position).getUser().getPhoto());

        return row;
    }
}
