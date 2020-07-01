package com.calenaur.pandemic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.model.event.Event;

public class EventCardView extends LinearLayout {

    private CardView card;
    private Event event;

    public EventCardView(Context context, Event event) {
        this(context, (AttributeSet) null);
        this.event = event;
        updateInfo();
    }

    public EventCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initLayout();
    }

    private void initLayout() {
        View view = inflate(getContext(), R.layout.view_event_card, this);
        card = view.findViewById(R.id.card);
        card.setMaxCardElevation(1f);
    }

    private void updateInfo() {
        if (event == null)
            return;

        TextView name = card.findViewById(R.id.name);
        TextView tier = card.findViewById(R.id.tier);
        TextView description = card.findViewById(R.id.description);

        name.setText(event.name);
        name.setTextColor(event.getTier().getColor());
        tier.setText(event.getTier().getName());
        tier.setTextColor(event.getTier().getColor());
        description.setText(event.description);
    }

}
