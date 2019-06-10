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
import android.widget.Toast;

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
        View itemView=LayoutInflater.from(context).inflate(R.layout.item_list_share,viewGroup,false);

        VHShare holder=new VHShare(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VHShare view=(VHShare)viewHolder;

        ShareItem item=items.get(position);

        view.tvTitle.setText(item.getTitle());
        view.tvNickname.setText(item.getNickName());
        if(item.getFilePath().length()<=51) {view.ivMap.setVisibility(View.GONE); view.ivMmap.setVisibility(View.GONE);}
        else Glide.with(context).load(item.getFilePath()).into(view.ivMap);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VHShare extends RecyclerView.ViewHolder{
        TextView tvTitle,tvNickname;
        ImageView ivMap,ivMmap;

        public VHShare(@NonNull final View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            ivMap=itemView.findViewById(R.id.iv_map);
            ivMmap=itemView.findViewById(R.id.iv_mmap);
            tvNickname=itemView.findViewById(R.id.tv_nickname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=getAdapterPosition();
                    context.startActivity(new Intent(context,BoardActivity.class).putExtra("item",items.get(index)));
                }
            });
        }
    }
}
