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
import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.model.user.UserEvent;
import com.calenaur.pandemic.view.EventCardView;
import com.calenaur.pandemic.view.TitledTextView;

public class EventFragment extends Fragment {

    private LinearLayout eventLayout;
    private SharedGameDataViewModel data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        data = ViewModelProviders.of(requireActivity()).get(SharedGameDataViewModel.class);
        eventLayout = view.findViewById(R.id.events);
        refresh();
    }

    public void refresh() {
        Bundle args = getArguments();
        if (args == null)
            return;

        LocalUser localUser = (LocalUser) args.get("local_user");
        if (localUser == null)
            return;

        UserEvent[] userEvents = data.getRegistrar().getUserEventRegistry().toArray(new UserEvent[]{});
        eventLayout.removeAllViews();
        for (UserEvent event : userEvents)
            if (event != null)
                eventLayout.addView(new EventCardView(getContext(), data.getRegistrar().getEventRegistry().get(event.id)));
    }
}