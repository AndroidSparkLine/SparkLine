package com.plenry.sparkline.View;

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
    private final List<Room> rooms;
    public  RoomArrayAdapter(Context context, int resource, List<Room> rooms) {
        super(context, resource, rooms);
        this.rooms = rooms;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        if (convertView == null) {
            // This is an expensive operation! Avoid and reuse as much as possible.
            row = inflater.inflate(R.layout.custom_row, parent, false);
        }

        TextView textView = (TextView) row.findViewById(R.id.label);
        textView.setText(rooms.get(position).getTopic());
        return row;
    }

}