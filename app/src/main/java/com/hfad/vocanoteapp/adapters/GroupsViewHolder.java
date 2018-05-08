package com.hfad.vocanoteapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.vocanoteapp.R;


public class GroupsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View swipeLayout;
    private TextView nameView;
    private ImageView iconEdit;
    private ImageView iconDelete;
    private OnItemClicked onClick;

    GroupsViewHolder(View itemView, OnItemClicked onClick) {
        super(itemView);
        this.onClick = onClick;
        swipeLayout = itemView.findViewById(R.id.surface);
        nameView = itemView.findViewById(R.id.group_name);
        iconEdit = itemView.findViewById(R.id.edit_group);
        iconDelete = itemView.findViewById(R.id.delete_group);
        iconEdit.setOnClickListener(this);
        iconDelete.setOnClickListener(this);
        swipeLayout.setOnClickListener(this);
    }

    public TextView getNameView() {
        return nameView;
    }

    @Override
    public void onClick(View v) {
        onClick.onItemClick(v, getAdapterPosition());
    }

}
