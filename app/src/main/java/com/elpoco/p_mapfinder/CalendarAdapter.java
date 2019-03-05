package com.elpoco.p_mapfinder;

import android.content.Context;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class CalendarAdapter extends RecyclerView.Adapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<CalendarItem> items;

    public CalendarAdapter(Context context, LayoutInflater inflater, ArrayList<CalendarItem> items) {
        this.context = context;
        this.inflater = inflater;
        this.items=items;
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
        vh.tvTime.setText(item.getTime());
        vh.tvName.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VHCalendar extends RecyclerView.ViewHolder {
        TextView tvDay,tvTime,tvName;
        public VHCalendar(@NonNull View itemView) {
            super(itemView);
            tvDay= itemView.findViewById(R.id.calendar_tv_day);
            tvTime= itemView.findViewById(R.id.calendar_tv_time);
            tvName= itemView.findViewById(R.id.calendar_tv_name);
        }
    }
}
