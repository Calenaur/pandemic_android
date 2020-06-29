package com.calenaur.pandemic.api.model.user;

import com.calenaur.pandemic.api.model.event.Event;
import com.calenaur.pandemic.api.register.Registry;

public class UserEvent {

    public String user;
    public int id;

    public Event getEvent(Registry<Event> eventRegistry) {
        return eventRegistry.get(id);
    }

}
