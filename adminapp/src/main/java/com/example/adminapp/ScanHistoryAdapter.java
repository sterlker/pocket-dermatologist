package com.example.adminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ScanHistoryAdapter extends BaseAdapter {
    Context context;
    List<ScanHistory> listScanHistory;

    public ScanHistoryAdapter(Context context, List<ScanHistory> listScanHistory) {
        this.context = context;
        this.listScanHistory = listScanHistory;
    }

    @Override
    public int getCount() {
        return listScanHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return listScanHistory.get(position); // Return the actual item
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the position as ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_sh, parent, false);
        }

        ImageView image_sh = convertView.findViewById(R.id.image_sh);
        TextView title_sh = convertView.findViewById(R.id.title_sh);
        TextView expand_sh = convertView.findViewById(R.id.expand_sh);

        title_sh.setText(listScanHistory.get(position).getTitle_sh());
        expand_sh.setText(listScanHistory.get(position).getExpand_sh());

        // Use Glide to load the image from the path
        Glide.with(context)
                .load(listScanHistory.get(position).getImage_sh()) // Use the path from getImage_sh()
                .placeholder(R.drawable.ic_camera) // Add a placeholder if needed
                .error(R.drawable.ic_error) // Add an error image if needed
                .into(image_sh);

        return convertView;
    }
}
