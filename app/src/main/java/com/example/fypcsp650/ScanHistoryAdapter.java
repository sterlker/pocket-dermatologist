package com.example.fypcsp650;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ScanHistoryAdapter extends BaseAdapter {
    Context context;
    List<ScanHistory> listScanHistory;

    public ScanHistoryAdapter(Context context, List<ScanHistory> listScanHistory) {
        this.context = context;
        this.listScanHistory = listScanHistory;
    }

    public int getCount(){
        return listScanHistory.size();
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_sh, parent, false);

        ImageView image_sh = view.findViewById(R.id.image_sh);
        TextView title_sh = view.findViewById(R.id.title_sh);
        TextView expand_sh = view.findViewById(R.id.expand_sh);

        title_sh.setText(listScanHistory.get(position).getTitle_sh());
        expand_sh.setText(listScanHistory.get(position).getExpand_sh());
        image_sh.setImageResource(listScanHistory.get(position).getImage_sh());

        return view;
    }
}
