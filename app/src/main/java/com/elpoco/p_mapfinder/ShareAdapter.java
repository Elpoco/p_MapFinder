package com.elpoco.p_mapfinder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShareAdapter extends RecyclerView.Adapter {

    ArrayList<ShareItem> items;
    Context context;

    public ShareAdapter(ArrayList<ShareItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);

        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH view=(VH)viewHolder;

        ShareItem item=items.get(position);

        view.tvTitle.setText(item.getTitle());
        Glide.with(context).load(item.getFilePath()).into(view.ivMap);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView ivMap;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            ivMap=itemView.findViewById(R.id.iv_map);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,BoardActivity.class));
                }
            });
        }
    }
}
