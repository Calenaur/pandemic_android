package com.calenaur.pandemic.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.calenaur.pandemic.R;

import com.calenaur.pandemic.api.model.user.Friend;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.ViewHolder> {

    private Friend[] mValues;

    public FriendRecyclerViewAdapter(Friend[] items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues[position];
        holder.mNameView.setText(mValues[position].getName());
        holder.mBalanceView.setText(String.valueOf(mValues[position].getBalance()));
        holder.mTierView.setText(String.valueOf(mValues[position].getTier()));
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mBalanceView;
        public final TextView mTierView;
        public Friend mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.name);
            mBalanceView = view.findViewById(R.id.balance);
            mTierView = view.findViewById(R.id.tier);
        }

        @NonNull
        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", mNameView=" + mNameView +
                    ", mBalanceView=" + mBalanceView +
                    ", mTierView=" + mTierView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}