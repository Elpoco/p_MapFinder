package com.elpoco.p_mapfinder;

import android.content.Context;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class CalendarAdapter extends RecyclerView.Adapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<CalendarItem> items;

    String[] icons;

    public CalendarAdapter(Context context, LayoutInflater inflater, ArrayList<CalendarItem> items) {
        this.context = context;
        this.inflater = inflater;
        this.items=items;
        icons=context.getResources().getStringArray(R.array.calendar_icon);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_list_calendar,viewGroup,false);
        VHCalendar vhCalendar=new VHCalendar(view);
        return vhCalendar;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VHCalendar vh= (VHCalendar) viewHolder;
        CalendarItem item=items.get(i);
        vh.tvDay.setText(item.getDay());
        vh.tvName.setText(item.getName());
        int index;
        for (index = 0; index < icons.length; index++) {
            if(item.getIcon().equals(icons[index])) break;
        }
        Glide.with(context).load(R.drawable.icon_calendar_01+index).into(vh.iv);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VHCalendar extends RecyclerView.ViewHolder {
        TextView tvDay,tvName;
        ImageView iv;
        public VHCalendar(@NonNull View itemView) {
            super(itemView);
            tvDay= itemView.findViewById(R.id.calendar_tv_day);
            tvName= itemView.findViewById(R.id.calendar_tv_name);
            iv=itemView.findViewById(R.id.calendar_icon);
        }
    }
}
