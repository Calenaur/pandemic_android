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
import com.calenaur.pandemic.view.FriendRequestCardView;

public class FriendRequestListFragment extends Fragment {

    private LinearLayout friendRequestsLayout;


    public FriendRequestListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_request_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        friendRequestsLayout = view.findViewById(R.id.friend_request_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedGameDataViewModel sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);

        Friend[] requests = sharedGameDataViewModel.getRequests();
        if (requests == null)
            return;

        if (requests.length < 1)
            return;

        friendRequestsLayout.removeAllViews();
        for (Friend request : requests) {
            FriendRequestCardView card = new FriendRequestCardView(getContext(), request, sharedGameDataViewModel);
            friendRequestsLayout.addView(card);
        }
    }
}