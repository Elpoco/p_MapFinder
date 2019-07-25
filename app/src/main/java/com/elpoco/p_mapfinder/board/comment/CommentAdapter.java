package com.elpoco.p_mapfinder.board.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.elpoco.p_mapfinder.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CommentItem> item;

    public CommentAdapter(Context context, ArrayList<CommentItem> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_list_comment,viewGroup,false);
        VHComment vhComment =new VHComment(view);
        return vhComment;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VHComment vhComment = (VHComment) viewHolder;
        CommentItem commentItem= item.get(i);
        vhComment.tvId.setText(commentItem.getId());
        vhComment.tvComment.setText(commentItem.getComment());
        Glide.with(context).load(commentItem.getProfileUrl()).into(vhComment.ivProfile);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class VHComment extends RecyclerView.ViewHolder {
        TextView tvId,tvComment;
        CircleImageView ivProfile;
        public VHComment(@NonNull View itemView) {
            super(itemView);
            tvId=itemView.findViewById(R.id.tv_comment_id);
            tvComment=itemView.findViewById(R.id.tv_comment_text);
            ivProfile=itemView.findViewById(R.id.iv_comment_user);
        }
    }

}
