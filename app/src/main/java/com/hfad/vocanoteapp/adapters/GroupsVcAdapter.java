package com.hfad.vocanoteapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.database.GroupVc;

import java.util.ArrayList;
import java.util.List;


public class GroupsVcAdapter extends
        RecyclerView.Adapter<GroupsViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private List<GroupVc> mGroupsVc;
    private List<GroupVc> filteredGroupsVc;
    private OnItemClicked onClick;
    private String query = "";

    public GroupsVcAdapter(Context context, OnItemClicked onClick) {
        mInflater = LayoutInflater.from(context);
        this.onClick = onClick;
        mGroupsVc = new ArrayList<>();
        filteredGroupsVc = new ArrayList<>();
    }

    public List<GroupVc> getFilteredGroupsVc() {
        return filteredGroupsVc;
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.layout_list_of_groups, parent, false);
        return new GroupsViewHolder(itemView, onClick);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupsViewHolder holder, int position) {

        if (filteredGroupsVc != null) {
            GroupVc current = filteredGroupsVc.get(position);
            holder.getNameView().setText(current.getNameGroup());
        } else {
            holder.getNameView().setText(R.string.nogroups);
        }
    }

    public void setGroupsVc(List<GroupVc> mGroupsVc) {
        this.mGroupsVc = mGroupsVc;
        getFilter().filter(query);
    }

    @Override
    public int getItemCount() {
        return filteredGroupsVc != null ? filteredGroupsVc.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredGroupsVc = (List<GroupVc>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                query = constraint.toString().toLowerCase();
                filteredGroupsVc.clear();
                if (query.isEmpty()) {
                    filteredGroupsVc.addAll(mGroupsVc);
                } else {
                    filteredGroupsVc = getFilteredResults(query);
                }
                FilterResults results = new FilterResults();
                results.values = filteredGroupsVc;
                results.count = filteredGroupsVc.size();
                return results;
            }
        };
    }

    private List<GroupVc> getFilteredResults(String constraint) {
        List<GroupVc> results = new ArrayList<>();

        for (GroupVc item : mGroupsVc) {
            if (item.getNameGroup().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
