package com.hfad.vocanoteapp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hfad.vocanoteapp.database.VocaNote;
import com.hfad.vocanoteapp.repository.WordRepository;

import java.util.List;


public class VocaNoteViewModel extends AndroidViewModel {

    private LiveData<List<VocaNote>> mAllWordsFrGroup;
    private WordRepository mRepository;

    public VocaNoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
    }

    public void insert(VocaNote vocaNote) {
        mRepository.insert(vocaNote);
    }

    public void deleteVocaNoteById(int id) {
        mRepository.deleteByIdVocaNote(id);
    }

    public void transferVocaNoteToStudiedById(int id) {
        mRepository.transferVocaNoteToStudiedById(id);
    }

    public void removeVocaNoteToStudiedById(int id) {
        mRepository.removeVocaNoteToStudiedById(id);
    }

    public LiveData<List<VocaNote>> getByGroupVcVocaNote(String nameGroup) {
        mAllWordsFrGroup = mRepository.getByGroupVcVocaNote(nameGroup);
        return mAllWordsFrGroup;
    }

    public List<VocaNote> searchVocaNoteByQuery(String nameGroup, String textQuery) {
        return mRepository.searchVocaNoteByQuery(nameGroup, textQuery);
    }

    public VocaNote getVocaNoteById(int id){
        return mRepository.getVocaNoteById(id);
    }

    public void editVocaNote(int id, String origWord, String translation){
        mRepository.editVocaNote(id, origWord, translation);
    }
}
