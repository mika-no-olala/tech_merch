package kz.smrtx.techmerch.items.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import kz.smrtx.techmerch.items.dao.UserDao;
import kz.smrtx.techmerch.items.entities.TechDatabase;
import kz.smrtx.techmerch.items.entities.User;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {
        TechDatabase db =TechDatabase.getInstance(application);
        userDao = db.userDao();
    }
    public void insertUsers(List<User> users) {
        new insertUserAsyncTask(userDao).execute(users);
    }
    public void deleteUsers() {
        new deleteUsersAsyncTask(userDao).execute();
    }
    public LiveData<List<User>> getUserList(int roleCode) {
        return userDao.getUserList(roleCode);
    }
    public LiveData<List<User>> getUsersByCityAndRole(int locationCode, int roleCode) {
        return userDao.getUsersByCityAndRole(locationCode, roleCode);
    }
    public int getUserRole(int userCode) { return userDao.getUserRole(userCode); }
    public int getNumberOfUsers(int locationCode, int roleCode) { return userDao.getNumberOfUsers(locationCode, roleCode); }

    private static class insertUserAsyncTask extends AsyncTask<List<User>, Void, Void>{
        private UserDao userDao;
        public insertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(List<User>... lists) {
            userDao.insertUsers(lists[0]);
            return null;
        }
    }

    private static class deleteUsersAsyncTask extends AsyncTask<Void, Void, Void>{
        private UserDao userDao;
        public deleteUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }
}
