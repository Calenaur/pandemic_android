package com.calenaur.pandemic.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.TextView;

import com.calenaur.pandemic.R;


public class BalanceFragment extends Fragment {

    private static final String BALANCE = "balance";

    private int balance_counter;

    public BalanceFragment() {
    }

    public static BalanceFragment newInstance(String param1) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putString(BALANCE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView balance = getActivity().findViewById(R.id.balance);
        balance.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        TextView balance = getActivity().findViewById(R.id.balance);
        balance.setVisibility(View.VISIBLE);
    }
}