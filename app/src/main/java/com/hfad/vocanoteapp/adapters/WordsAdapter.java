package com.hfad.vocanoteapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.database.VocaNote;

import java.util.ArrayList;
import java.util.List;

public class WordsAdapter extends
        RecyclerView.Adapter<WordsViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private List<VocaNote> mVocaNotes;
    private List<VocaNote> filteredVocaNotes;
    private OnItemClicked onClick;
    private String query = "";

    public WordsAdapter(Context context, OnItemClicked onClick) {
        mInflater = LayoutInflater.from(context);
        this.onClick = onClick;
        mVocaNotes = new ArrayList<>();
        filteredVocaNotes = new ArrayList<>();
    }

    public List<VocaNote> getFilteredVocaNotes() {
        return filteredVocaNotes;
    }

    @NonNull
    @Override
    public WordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.layout_list_of_words, parent, false);
        return new WordsViewHolder(itemView, onClick);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsViewHolder holder, int position) {
        if (filteredVocaNotes != null) {
            VocaNote current = filteredVocaNotes.get(position);
            holder.getWordNum().setText(String.valueOf(position + 1));
            holder.getOrigWord().setText(current.getOriginWord());
            holder.getTransWord().setText(current.getTranslation());

        } else {
            Log.i("INF", "No words in this group.");
        }
    }

    @Override
    public int getItemCount() {
        return filteredVocaNotes != null ? filteredVocaNotes.size() : 0;
    }

    public void setVocaNotes(List<VocaNote> mVocaNotes) {
        this.mVocaNotes = mVocaNotes;
        getFilter().filter(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredVocaNotes = (List<VocaNote>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                query = constraint.toString().toLowerCase();
                filteredVocaNotes.clear();
                if (query.isEmpty()) {
                    filteredVocaNotes.addAll(mVocaNotes);
                } else {
                    filteredVocaNotes = getFilteredResults(query);
                }

                FilterResults results = new FilterResults();
                results.values = filteredVocaNotes;
                results.count = filteredVocaNotes.size();
                return results;
            }
        };
    }

    private List<VocaNote> getFilteredResults(String constraint) {
        List<VocaNote> results = new ArrayList<>();

        for (VocaNote item : mVocaNotes) {
            if ((item.getOriginWord().toLowerCase().contains(constraint))
                    || (item.getTranslation().toLowerCase().contains(constraint))) {
                results.add(item);
            }
        }
        return results;
    }
}
