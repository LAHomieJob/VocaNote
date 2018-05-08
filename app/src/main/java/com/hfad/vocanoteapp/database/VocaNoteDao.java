package com.hfad.vocanoteapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;


@Dao
public interface VocaNoteDao {

    @Query("SELECT * FROM vocanote")
    List<VocaNote> getAllVocaNote();

    @Query("SELECT * FROM vocanote WHERE name_group = :nameGroup " +
            "AND (origin_word = :textQuery OR translation = :textQuery)")
    List<VocaNote> searchVocaNoteByQuery(String nameGroup, String textQuery);

    @Query("DELETE FROM vocanote WHERE id = :id")
    void deleteByIdVocaNote(int id);

    @Query("SELECT * FROM vocanote WHERE name_group = :nameGroup")
    LiveData<List<VocaNote>> getByGroupVcVocaNote(String nameGroup);

    @Insert
    void insert(VocaNote vocaNote);

    @Update
    void update(VocaNote vocaNote);

    @Delete
    void delete(VocaNote vocaNote);
}
