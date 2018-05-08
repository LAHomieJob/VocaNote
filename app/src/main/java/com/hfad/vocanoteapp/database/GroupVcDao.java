package com.hfad.vocanoteapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GroupVcDao {

    @Insert
    void insert(GroupVc groupVc);

    @Query("SELECT * FROM groupvc")
    LiveData<List<GroupVc>> getAllGroupVc();

    @Query("SELECT * FROM groupvc WHERE name_group = :nameGroup")
    GroupVc getByGroupVcName(String nameGroup);

    @Query("SELECT * FROM groupvc WHERE name_group = :nameGroup")
    List<GroupVc> getFilteredGroupVc(String nameGroup);

    @Query("DELETE FROM groupvc WHERE name_group = :nameGroup")
    void deleteByGroupVcName(String nameGroup);

    @Query("UPDATE groupvc SET name_group= :newName WHERE name_group= :currentName")
    void updateNameOfGroup(String currentName, String newName);
}
