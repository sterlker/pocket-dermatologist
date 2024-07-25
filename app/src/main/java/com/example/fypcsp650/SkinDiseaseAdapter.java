package com.example.fypcsp650;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SkinDiseaseAdapter extends BaseAdapter {
    Context context;
    List<SkinDisease> listSkinDisease;

    public SkinDiseaseAdapter(Context context, List<SkinDisease> listSkinDisease) {
        this.context = context;
        this.listSkinDisease = listSkinDisease;
    }

    public int getCount(){
        return listSkinDisease.size();
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent){
        view = LayoutInflater.from(context).inflate(R.layout.list_item_sd, parent, false);

        TextView title_sd = view.findViewById(R.id.title_sd);
        TextView expand_sd = view.findViewById(R.id.expand_sd);

        title_sd.setText(listSkinDisease.get(position).getTitle_sd());
        expand_sd.setText(listSkinDisease.get(position).getExpand_sd());

        return view;
    }
}
