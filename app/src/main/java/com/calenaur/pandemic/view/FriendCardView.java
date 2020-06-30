package com.calenaur.pandemic.view;

import android.app.AlertDialog;
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

public class FriendCardView extends LinearLayout {

    private CardView card;

    public FriendCardView(Context context, Friend friend, SharedGameDataViewModel sharedGameDataViewModel) {
        this(context, null);
        if (friend == null)
            return;

        TextView name = card.findViewById(R.id.name);
        TextView balance = card.findViewById(R.id.balance);
        TextView tier = card.findViewById(R.id.tier);
        ImageButton button = card.findViewById(R.id.remove_friend_button);

        name.setText(friend.getName());
        tier.setText(String.valueOf(friend.getTier()));
        balance.setText(String.valueOf(friend.getBalance()));
        button.setOnClickListener((e) ->{
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.remove_friend)
                    .setCancelable(true)
                    .setMessage(getResources().getString(R.string.remove_friend_message))
                    .setPositiveButton(getResources().getString(R.string.remove), (dialog1, which) -> {
                        sharedGameDataViewModel.removeFriend(friend.getName());
                        Toast.makeText(getContext(),R.string.friend_removed, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel, (dialog1, which) -> {
                        dialog1.cancel();
                    })
                    .create();
            dialog.show();
        });
    }
    public FriendCardView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        View view = inflate(getContext(), R.layout.view_friend_card, this);
        card = view.findViewById(R.id.card);
        card.setMaxCardElevation(1f);
    }
}
