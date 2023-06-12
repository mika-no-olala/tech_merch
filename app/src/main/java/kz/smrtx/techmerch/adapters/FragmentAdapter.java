package kz.smrtx.techmerch.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kz.smrtx.techmerch.fragments.RSAdvancingFragment;
import kz.smrtx.techmerch.fragments.RSFinishedFragment;
import kz.smrtx.techmerch.fragments.RSRequestFragment;
import kz.smrtx.techmerch.fragments.RSWaitingFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    Fragment rsWaiting, rsAdvancing, rsFinished, rsRequest;
    public static FragmentAdapter instance;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        instance = this;
    }

    public static FragmentAdapter getInstance() {
        return instance;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("sssFRAGMENT", String.valueOf(position));
        switch (position) {
            case 0:
                rsRequest = new RSRequestFragment();
                return rsRequest;
            case 1:
                rsWaiting = new RSWaitingFragment();
                return rsWaiting;
            case 2:
                rsAdvancing = new RSAdvancingFragment();
                return rsAdvancing;
            case 3:
                rsFinished = new RSFinishedFragment();
                return rsFinished;

        }
        return new RSWaitingFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public Fragment getRsWaiting() {
        return rsWaiting;
    }

    public Fragment getRsAdvancing() {
        return rsAdvancing;
    }

    public Fragment getRsFinished() {
        return rsFinished;
    }

    public Fragment getRsRequest() {
        return rsRequest;
    }

    public void resetRsRequest() {
        rsRequest = new RSRequestFragment();
    }


}
