package com.example.xiyou3g.mqttdemo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiyou3g.mqttdemo.Bean.ComBean;
import com.example.xiyou3g.mqttdemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lance on 2017/8/2.
 */

public class CommuAdapter extends RecyclerView.Adapter<CommuAdapter.GuideViewHolder> {

    private List<ComBean> guideBeanList = new ArrayList<>();

    public CommuAdapter(List<ComBean> guideBeen){
        guideBeanList = guideBeen;
    }

    @Override
    public GuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commu_item,parent,false);
        GuideViewHolder viewHolder = new GuideViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GuideViewHolder holder, int position) {
        ComBean guideBean = guideBeanList.get(position);
        if(guideBean.getType() == ComBean.SEND_TYPE){
            holder.leftlayout.setVisibility(View.GONE);
            holder.rightlayout.setVisibility(View.VISIBLE);
            holder.rightmsg.setText(guideBean.getGuideMsg());
            holder.rightuser.setText(guideBean.getUser());
        }else{
            holder.rightlayout.setVisibility(View.GONE);
            holder.leftlayout.setVisibility(View.VISIBLE);
            holder.leftmsg.setText(guideBean.getGuideMsg());
            holder.leftuser.setText(guideBean.getUser());
        }
    }

    @Override
    public int getItemCount() {
        return guideBeanList.size();
    }

    static class GuideViewHolder extends RecyclerView.ViewHolder{

        private TextView leftmsg;
        private TextView rightmsg;
        private TextView leftuser;
        private TextView rightuser;
        private LinearLayout leftlayout;
        private LinearLayout rightlayout;

        public GuideViewHolder(View view) {
            super(view);
            leftmsg = (TextView) view.findViewById(R.id.left_msg);
            rightmsg = (TextView) view.findViewById(R.id.right_msg);
            leftuser = (TextView) view.findViewById(R.id.left_user);
            rightuser = (TextView) view.findViewById(R.id.right_user);
            leftlayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightlayout = (LinearLayout) view.findViewById(R.id.right_layout);
        }
    }
}
