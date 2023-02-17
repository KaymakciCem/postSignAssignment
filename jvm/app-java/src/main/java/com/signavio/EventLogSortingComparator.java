package com.signavio;

import java.util.Comparator;

public class EventLogSortingComparator implements Comparator<EventlogRow> {

    @Override
    public int compare(EventlogRow o1, EventlogRow o2) {
        if (o1.getTimestamp().isEqual(o2.getTimestamp())) {
            return o1.getEventName().compareTo(o2.getEventName());
        }

        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
