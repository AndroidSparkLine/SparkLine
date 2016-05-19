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
        if (convertView == null) {cd
            // This is an expensive operation! Avoid and reuse as much as possible.
            row = inflater.inflate(R.layout.message_layout, parent, false);
        }
        new DownloadImageTask((ImageView) row.findViewById(R.id.imageIconLeft))
                .execute(messages.get(position).getUser().getPhoto());
        //ImageView leftImage = (ImageView)row.findViewById(R.id.imageIconLeft);
        //leftImage.setImageResource();

        TextView messageView = (TextView) row.findViewById(R.id.messageTxt);
        messageView.setText(messages.get(position).getContent());

        messageView.setText(messages.get(position).getContent());
        ImageView rightImage = (ImageView) row.findViewById(R.id.imageIconRight);
        new DownloadImageTask((ImageView) row.findViewById(R.id.imageIconRight))
                .execute(messages.get(position).getUser().getPhoto());

        return row;
    }
//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }


}
