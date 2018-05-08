package com.hfad.vocanoteapp.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = GroupVc.class,
        parentColumns = "name_group",
        childColumns = "name_group",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class VocaNote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "origin_word")
    private String originWord;

    @ColumnInfo(name = "translation")
    private String translation;

    @ColumnInfo(name = "studied_word")
    private int studied;

    @ColumnInfo(name = "name_group")
    private String nameGroup;

    public VocaNote(String originWord, String translation, String nameGroup) {
        this.originWord = originWord;
        this.translation = translation;
        this.studied = 0;
        this.nameGroup = nameGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginWord() {
        return originWord;
    }

    public void setOriginWord(String originWord) {
        this.originWord = originWord;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getStudied() {
        return studied;
    }

    public void setStudied(int studied) {
        this.studied = studied;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }
}


