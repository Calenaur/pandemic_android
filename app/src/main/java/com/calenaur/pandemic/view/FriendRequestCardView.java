package com.calenaur.pandemic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.calenaur.pandemic.R;
import com.calenaur.pandemic.SharedGameDataViewModel;
import com.calenaur.pandemic.api.model.user.Friend;

public class FriendRequestCardView extends LinearLayout {

    private CardView card;

    public FriendRequestCardView(Context context, Friend friend, SharedGameDataViewModel sharedGameDataViewModel) {
        this(context, (AttributeSet) null);
        if (friend == null)
            return;

        TextView name = card.findViewById(R.id.name);
        ImageButton acceptButton = card.findViewById(R.id.accept_friend_button);
        ImageButton declineButton = card.findViewById(R.id.decline_friend_button);

        name.setText(friend.getName());
        acceptButton.setOnClickListener((e) ->{
            sharedGameDataViewModel.sendFriendResponse(friend.getName(), 1);
            Toast.makeText(getContext(),getResources().getString(R.string.friend_accepted)+" "+ friend.getName(), Toast.LENGTH_SHORT).show();
        });
        declineButton.setOnClickListener((e) ->{
            sharedGameDataViewModel.sendFriendResponse(friend.getName(), 2);
        });
    }
    public FriendRequestCardView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        View view = inflate(getContext(), R.layout.view_friend_request_card, this);
        card = view.findViewById(R.id.card);
        card.setMaxCardElevation(1f);


    }
}
