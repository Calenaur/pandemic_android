package com.calenaur.pandemic.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.adapter.FriendRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class FriendFragment extends Fragment {

    private SharedGameDataViewModel sharedGameDataViewModel;
    private FloatingActionButton addButton;
    private EditText input;
    private RecyclerView recyclerView;

    public FriendFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.list);
        addButton = view.findViewById(R.id.add_friend);
        addButton.setOnClickListener(this::onAddFriendClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);

        // Set the adapter
        recyclerView.setAdapter(new FriendRecyclerViewAdapter(sharedGameDataViewModel.getFriends()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void onAddFriendClick(View v) {
        Context c = getContext();
        input = new EditText(c);

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.research_new)
                .setCancelable(true)
                .setMessage(getResources().getString(R.string.add_new_friend_message))
                .setView(input)
                .setPositiveButton(getResources().getString(R.string.add_friend), this::onNewFriend)
                .setNegativeButton(R.string.cancel, this::onAddFriendCancel)
                .create();
        dialog.show();
    }

    private void onNewFriend(DialogInterface dialog, int which) {
        String inputValue = input.getText().toString();
        if (!inputValue.equals("")){
            sharedGameDataViewModel.addFriend(inputValue);
        }
        dialog.dismiss();
        Navigation.findNavController(addButton).navigate(R.id.friendFragment);
    }

    private void onAddFriendCancel(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}