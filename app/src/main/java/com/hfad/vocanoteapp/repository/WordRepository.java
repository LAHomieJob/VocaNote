package com.hfad.vocanoteapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.hfad.vocanoteapp.database.AppDatabase;
import com.hfad.vocanoteapp.database.VocaNote;
import com.hfad.vocanoteapp.database.VocaNoteDao;

import java.util.List;


public class WordRepository {
    private LiveData<List<VocaNote>> mAllWordsFrGroup;
    private List<VocaNote> query;
    private VocaNoteDao mVocaNoteDao;

    public WordRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mVocaNoteDao = db.vocaNoteDao();
    }

    public List<VocaNote> searchVocaNoteByQuery(String nameGroup, String textQuery) {
        query = mVocaNoteDao.searchVocaNoteByQuery(nameGroup, textQuery);
        return query;
    }

    public LiveData<List<VocaNote>> getByGroupVcVocaNote(String nameGroup) {
        mAllWordsFrGroup = mVocaNoteDao.getByGroupVcVocaNote(nameGroup);
        return mAllWordsFrGroup;
    }

    public void insert(VocaNote vocaNote) {
        new insertAsyncTask(mVocaNoteDao).execute(vocaNote);
    }

    public void deleteByIdVocaNote(int id) {
        new deleteByIdVocaNoteAsyncTask(mVocaNoteDao).execute(id);
    }

    private static class insertAsyncTask extends AsyncTask<VocaNote, Void, Void> {

        private VocaNoteDao mAsyncTaskDao;

        insertAsyncTask(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocaNote... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteByIdVocaNoteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private VocaNoteDao mAsyncTaskDao;

        deleteByIdVocaNoteAsyncTask(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteByIdVocaNote(params[0]);
            return null;
        }
    }
}

