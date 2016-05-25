package com.plenry.sparkline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plenry.sparkline.R;
import com.plenry.sparkline.bean.Room;

import java.util.List;

/**
 * Created by Xiaoyu on 5/16/16.
 */
public class RoomArrayAdapter extends ArrayAdapter<Room> {
    private List<Room> rooms;
    private TextView textView;

    public RoomArrayAdapter(Context context, int resource, List<Room> rooms) {
        super(context, resource, rooms);
        this.rooms = rooms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row, parent, false);
            textView = (TextView) convertView.findViewById(R.id.label);
        }
        textView.setText(rooms.get(position).getTopic());
        return convertView;
    }

}