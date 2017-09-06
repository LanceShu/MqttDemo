package com.example.xiyou3g.mqttdemo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiyou3g.mqttdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lance on 2017/8/6.
 */

public class PupAdapter extends RecyclerView.Adapter<PupAdapter.UserViewHolder> {

    private List<String> username = new ArrayList<>();
    private int[] colorID = {R.color.blue,R.color.colorAccent,R.color.green,R.color.red,R.color.yellow};

    public PupAdapter(List<String> usernamel){
        username = usernamel;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.usercolor.setBackgroundResource(colorID[position%5]);
        holder.userName.setText(username.get(position));
    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView usercolor;
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);
            usercolor = (TextView) itemView.findViewById(R.id.user_colors);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }
}

