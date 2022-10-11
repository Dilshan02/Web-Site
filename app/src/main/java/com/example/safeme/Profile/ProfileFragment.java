package com.example.safeme.Profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.safeme.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    public ProfileViewModel getmViewModel() {
        return mViewModel;
    }

    public ProfileFragment(ProfileViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    public ProfileFragment() {
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return super.getDefaultViewModelProviderFactory();
    }

    public ProfileFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    public ProfileFragment(int contentLayoutId, ProfileViewModel mViewModel) {
        super(contentLayoutId);
        this.mViewModel = mViewModel;
    }

    public void setmViewModel(ProfileViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}