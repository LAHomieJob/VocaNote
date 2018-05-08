package com.hfad.vocanoteapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hfad.vocanoteapp.database.GroupVc;
import com.hfad.vocanoteapp.repository.GroupRepository;

import java.util.List;

public class GroupsViewModel extends AndroidViewModel {

    private LiveData<List<GroupVc>> mAllGroups;
    private GroupRepository mRepository;

    public GroupsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GroupRepository(application);
        mAllGroups = mRepository.getAllGroupVc();
    }

    public LiveData<List<GroupVc>> getAllGroups() {
        return mAllGroups;
    }

    public void insert(GroupVc groupVc) { mRepository.insert(groupVc); }

    public void delete(String nameGroup) { mRepository.delete(nameGroup); }

    public GroupVc getGroupByName(String nameGroup) {
        return mRepository.getGroupByName(nameGroup);
    }

    public void updateGroupByName(String currentName, String newName) {
        mRepository.updateGroupByName(currentName, newName);
    }
}
