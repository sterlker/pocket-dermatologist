package com.example.adminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    Context context;
    List<User> listUser;

    public UserAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public int getCount(){
        return listUser.size();
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_sd, parent, false);

        TextView title_user = view.findViewById(R.id.title_sd);
        TextView expand_user = view.findViewById(R.id.expand_sd);

        title_user.setText(listUser.get(position).getUsername());
        expand_user.setText(listUser.get(position).getExpand_user());

        return view;
    }
}
