package com.hfad.vocanoteapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hfad.vocanoteapp.database.AppDatabase;
import com.hfad.vocanoteapp.database.VocaNote;
import com.hfad.vocanoteapp.database.VocaNoteDao;

import java.util.List;
import java.util.concurrent.ExecutionException;


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

    public VocaNote getVocaNoteById(int id) {
        try {
            return new getVocaNoteByIdAsyncTask(mVocaNoteDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void editVocaNote(int vocaNoteId, String origWord, String translation){
        EditTaskParams params = new EditTaskParams(vocaNoteId, origWord, translation);
        new editVocaNoteAsyncTask(mVocaNoteDao).execute(params);
    }

    public void transferVocaNoteToStudiedById(int id) {
        new transferVocaNoteToStudiedAsyncTaskById(mVocaNoteDao).execute(id);
    }

    public void removeVocaNoteToStudiedById(int id) {
        new removeVocaNoteFromStudiedAsyncTaskById(mVocaNoteDao).execute(id);
    }

    public void deleteByIdVocaNote(int id) {
        new deleteByIdVocaNoteAsyncTask(mVocaNoteDao).execute(id);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<VocaNote, Void, Void> {

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

    private static class EditTaskParams {
        int vocaNoteId;
        String origWord;
        String translation;

        EditTaskParams(int vocaNoteId, String origWord, String translation) {
            this.vocaNoteId = vocaNoteId;
            this.origWord = origWord;
            this.translation = translation;
        }
    }

    private static class editVocaNoteAsyncTask extends AsyncTask<EditTaskParams, Void, Void> {

        private VocaNoteDao mAsyncTaskDao;

        editVocaNoteAsyncTask(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(EditTaskParams... params) {
            int vocaNoteId = params[0].vocaNoteId;
            String origWord = params[0].origWord;
            String translation = params[0].translation;
            mAsyncTaskDao.changeVocaNote(vocaNoteId, origWord, translation);
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

    private static class transferVocaNoteToStudiedAsyncTaskById extends AsyncTask<Integer, Void, Void> {

        private VocaNoteDao mAsyncTaskDao;

        transferVocaNoteToStudiedAsyncTaskById(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.transferVocaNoteToStudiedById(params[0]);
            return null;
        }
    }

    private static class removeVocaNoteFromStudiedAsyncTaskById extends AsyncTask<Integer, Void, Void> {

        private VocaNoteDao mAsyncTaskDao;

        removeVocaNoteFromStudiedAsyncTaskById(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.removeVocaNoteFromStudiedById(params[0]);
            return null;
        }
    }

    private static class getVocaNoteByIdAsyncTask extends AsyncTask<Integer, Void, VocaNote> {

        private VocaNoteDao mAsyncTaskDao;

        getVocaNoteByIdAsyncTask(VocaNoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected VocaNote doInBackground(Integer... params) {
            return mAsyncTaskDao.getByIdVocaNote(params[0]);
        }
    }
}

