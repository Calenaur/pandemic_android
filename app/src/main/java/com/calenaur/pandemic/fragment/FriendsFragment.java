package com.calenaur.pandemic.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private SharedGameDataViewModel sharedGameDataViewModel;
    private FloatingActionButton addButton;
    private EditText input;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addButton = view.findViewById(R.id.add_friend);
        addButton.setOnClickListener(this::onAddFriendClick);

         viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        FriendListFragment friendListFragment = new FriendListFragment();
        FriendRequestListFragment friendRequestListFragment = new FriendRequestListFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(friendListFragment, getResources().getString(R.string.friends));
        viewPagerAdapter.addFragment(friendRequestListFragment, getResources().getString(R.string.request));

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedGameDataViewModel = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
    }

    private void onAddFriendClick(View v) {
        Context c = getContext();
        input = new EditText(c);

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.add_friend)
                .setCancelable(true)
                .setMessage(getResources().getString(R.string.add_new_friend_message))
                .setView(input)
                .setPositiveButton(getResources().getString(R.string.add_friend), this::onNewFriend)
                .setNegativeButton(R.string.cancel, this::onAddFriendCancel)
                .create();
        dialog.show();
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    private void onNewFriend(DialogInterface dialog, int which) {
        String inputValue = input.getText().toString();
        if (!inputValue.equals("")){
            sharedGameDataViewModel.addFriend(inputValue);
            Toast.makeText(getContext(),R.string.friend_request_send, Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
        Navigation.findNavController(addButton).navigate(R.id.friendsFragment);
    }

    private void onAddFriendCancel(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}
