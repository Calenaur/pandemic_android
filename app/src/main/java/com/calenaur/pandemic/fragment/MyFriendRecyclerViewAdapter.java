package com.calenaur.pandemic.fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.calenaur.pandemic.R;

import com.calenaur.pandemic.api.model.user.Friend;

import java.util.List;

public class MyFriendRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendRecyclerViewAdapter.ViewHolder> {

    private final List<Friend> mValues;

    public MyFriendRecyclerViewAdapter(List<Friend> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mBalanceView.setText(mValues.get(position).getBalance());
        holder.mTierView.setText(mValues.get(position).getTier());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        @Override
        public String toString() {
            return super.toString() + " '" + mBalanceView.getText() + "'";
        }
    }
}