package com.calenaur.pandemic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.view.TitledTextView;


public class AccountFragment extends Fragment {

    private TitledTextView guid;
    private TitledTextView username;
    private TitledTextView access;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        guid = view.findViewById(R.id.guid);
        username = view.findViewById(R.id.username);
        access = view.findViewById(R.id.access);
        refresh();
    }

    public void refresh() {
        Bundle args = getArguments();
        if (args == null)
            return;

        LocalUser localUser = (LocalUser) args.get("local_user");
        if (localUser == null)
            return;

        guid.setText(localUser.getUid());
        username.setText(localUser.getUsername());
        access.setText(String.valueOf(localUser.getAccessLevel()));
    }

}