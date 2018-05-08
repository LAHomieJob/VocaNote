package com.hfad.vocanoteapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class GroupVc {

    @ColumnInfo(name = "language")
    private String language;


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "name_group")

    private String nameGroup;

    public GroupVc(String language, String nameGroup) {
        this.language = language;
        this.nameGroup = nameGroup;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @NonNull
    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(@NonNull String nameGroup) {
        this.nameGroup = nameGroup;
    }
}
