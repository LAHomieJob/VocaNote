package com.hfad.vocanoteapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.vocanoteapp.R;

public class WordsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnItemClicked onClick;
    private ImageView soundIcon;
    private TextView wordNum;
    private TextView origWord;
    private TextView transWord;

    public WordsViewHolder(View itemView, OnItemClicked onClick) {
        super(itemView);
        this.onClick = onClick;
        soundIcon = itemView.findViewById(R.id.word_sound);
        wordNum = itemView.findViewById(R.id.wordNum);
        origWord = itemView.findViewById(R.id.originWord);
        transWord = itemView.findViewById(R.id.translation);
        soundIcon.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    public TextView getWordNum() {
        return wordNum;
    }

    public TextView getOrigWord() {
        return origWord;
    }

    public TextView getTransWord() {
        return transWord;
    }

    @Override
    public void onClick(View v) {
        onClick.onItemClick(v, getAdapterPosition());
    }
}
