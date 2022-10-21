package kz.smrtx.techmerch.items.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import kz.smrtx.techmerch.items.entities.User;
import kz.smrtx.techmerch.items.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }
    public void insertUsers(List<User> users) {
        userRepository.insertUsers(users);
    }
    public void deleteUsers() {
        userRepository.deleteUsers();
    }
    public LiveData<List<User>> getUserList(int roleCode) {
        return userRepository.getUserList(roleCode);
    }

    public LiveData<List<User>> getUserList(int locationCode, int roleCode) {
        return userRepository.getUsersByCityAndRole(locationCode, roleCode);
    }

    public int getUserRole(int userCode) {
        return userRepository.getUserRole(userCode);
    }
    public int getNumberOfUsers(int locationCode, int roleCode) {
        return userRepository.getNumberOfUsers(locationCode, roleCode);
    }



}
