package com.hfad.vocanoteapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hfad.vocanoteapp.ui.vocaNoteActivityController.VocaNoteContainerFragment;

public class VocaNotePagerAdapter extends FragmentStatePagerAdapter {
    private String nameGroup;
    private int countVocaNotes;

    public VocaNotePagerAdapter(FragmentManager fm, String nameGroup) {
        super(fm);
        this.nameGroup = nameGroup;
    }

    public void setCountVocaNotes(int countVocaNotes) {
        this.countVocaNotes = countVocaNotes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countVocaNotes;
    }


    @Override
    public Fragment getItem(int position) {
        return new VocaNoteContainerFragment().newInstance(position, nameGroup);
    }
}
