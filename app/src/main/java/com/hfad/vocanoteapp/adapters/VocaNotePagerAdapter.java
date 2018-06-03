package com.hfad.vocanoteapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.hfad.vocanoteapp.ui.vocaNoteActivityController.VocaNoteContainerFragment;

public class VocaNotePagerAdapter extends FragmentStatePagerAdapter {
    private String nameGroup;
    private String language;
    private int countVocaNotes;

    public VocaNotePagerAdapter(final FragmentManager fm, String nameGroup, String language) {
        super(fm);
        this.nameGroup = nameGroup;
        this.language = language;
    }


    public void setCountVocaNotes(int countVocaNotes) {
        this.countVocaNotes = countVocaNotes;
        Log.d("ADAPTER_SIZE", String.valueOf(countVocaNotes));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countVocaNotes;
    }


    @Override
    public Fragment getItem(int position) {
        return new VocaNoteContainerFragment().newInstance(position, nameGroup, language);
    }
}
