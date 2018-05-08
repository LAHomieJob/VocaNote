package com.hfad.vocanoteapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hfad.vocanoteapp.database.AppDatabase;
import com.hfad.vocanoteapp.database.GroupVc;
import com.hfad.vocanoteapp.database.GroupVcDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GroupRepository {
    private LiveData<List<GroupVc>> mAllGroups;
    private GroupVcDao mGroupVcDao;

    public GroupRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mGroupVcDao = db.groupVcDao();
        mAllGroups = mGroupVcDao.getAllGroupVc();
    }

    public LiveData<List<GroupVc>> getAllGroupVc() {
        return mAllGroups;
    }

    public void insert(GroupVc groupVc) {
        new insertAsyncTask(mGroupVcDao).execute(groupVc);
    }

    public void delete(String nameGroup) {
        new deleteAsyncTask(mGroupVcDao).execute(nameGroup);
    }

    public GroupVc getGroupByName(String nameGroup) {
        try {
            return new getAsyncTask(mGroupVcDao).execute(nameGroup).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateGroupByName(String currentName, String newName) {
        new updateByNameGroupAsyncTask(mGroupVcDao).execute(currentName, newName);
    }

    private static class insertAsyncTask extends AsyncTask<GroupVc, Void, Void> {

        private GroupVcDao mAsyncTaskDao;

        insertAsyncTask(GroupVcDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GroupVc... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private GroupVcDao mAsyncTaskDao;

        deleteAsyncTask(GroupVcDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.deleteByGroupVcName(params[0]);
            return null;
        }
    }

    private static class updateByNameGroupAsyncTask extends AsyncTask<String, Void, Void> {

        private GroupVcDao mAsyncTaskDao;

        updateByNameGroupAsyncTask(GroupVcDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.updateNameOfGroup(params[0], params[1]);
            return null;
        }
    }

    private static class getAsyncTask extends AsyncTask<String, Void, GroupVc> {

        private GroupVcDao mAsyncTaskDao;

        getAsyncTask(GroupVcDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected GroupVc doInBackground(String... params) {
            return mAsyncTaskDao.getByGroupVcName(params[0]);
        }

        @Override
        protected void onPostExecute(GroupVc groupVc) {
            super.onPostExecute(groupVc);
        }
    }
}
