package com.calenaur.pandemic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.user.Friend;
import com.calenaur.pandemic.view.FriendCardView;


public class FriendListFragment extends Fragment {

    private LinearLayout friendsLayout;

    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        friendsLayout = view.findViewById(R.id.friends_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedGameDataViewModel sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);

        Friend[] friends = sharedGameDataViewModel.getFriends();
        if (friends == null)
            return;

        if (friends.length < 1)
            return;

        friendsLayout.removeAllViews();
        for (Friend friend : friends) {
            FriendCardView card = new FriendCardView(getContext(), friend, sharedGameDataViewModel);
            friendsLayout.addView(card);
        }
    }


}