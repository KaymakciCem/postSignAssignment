package com.signavio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProcureToPayActivity {

    public static final int NUMBER_OF_RECORDS_SHOWN = 10;
    private static Map<String, TreeSet<EventlogRow>> activitiesMap = new ConcurrentHashMap<>();
    private static Map<String, AtomicInteger> resultMap = new ConcurrentHashMap<>();

    public static Map<String, TreeSet<EventlogRow>> getActivitiesMap() {
        return activitiesMap;
    }

    public static void groupActivitiesTreeSet(final int startIndex, final int endIndex, final List<EventlogRow> activitiesLog) {
        final EventLogSortingComparator comparator = new EventLogSortingComparator();

        for (int i = startIndex; i < endIndex; i++) {
            final String key = activitiesLog.get(i).getCaseId();
            if (activitiesMap.containsKey(key)) {
                TreeSet<EventlogRow> eventRows = activitiesMap.get(key);
                EventlogRow eventlogRow = new EventlogRow(activitiesLog.get(i).getEventName(), activitiesLog.get(i).getTimestamp());
                eventRows.add(eventlogRow);
            } else {
                final TreeSet<EventlogRow> ts = new TreeSet<>(comparator);
                final EventlogRow eventlogRow =
                        new EventlogRow(activitiesLog.get(i).getEventName(), activitiesLog.get(i).getTimestamp());
                ts.add(eventlogRow);
                activitiesMap.put(activitiesLog.get(i).getCaseId(), ts);
            }
        }
    }

    public static void createStatistics(final String key) {
        final TreeSet<EventlogRow> groupedEventLogs = activitiesMap.get(key);
        final String groupName = groupedEventLogs
                .stream()
                .map(EventlogRow::getEventName)
                .collect(Collectors.joining(","));

        final long count = groupedEventLogs.size();

        final String mapKey = count + " Steps: " + groupName;
        if (resultMap.containsKey(mapKey)) {
            resultMap.get(mapKey).incrementAndGet();
        } else {
            resultMap.computeIfAbsent(mapKey, k -> new AtomicInteger(1));
        }
    }

    public static void print() {
        final ValueComparator valueComparator = new ValueComparator(resultMap);
        final TreeMap<String, AtomicInteger> sortedTreeMap = new TreeMap<>(valueComparator);
        sortedTreeMap.putAll(resultMap);

        final Map<String, Integer> firstTen = new LinkedHashMap<>();
        for(int i = 0; i < NUMBER_OF_RECORDS_SHOWN; i ++) {
            Map.Entry<String, AtomicInteger> e = sortedTreeMap.pollFirstEntry();
            System.out.println(e.getKey().substring(0, 30) + "... " + ": " + e.getValue());
            firstTen.put(e.getKey().substring(0, 30) + "... " + ": ", e.getValue().get());
        }

//        final JSONObject jsonObject = new JSONObject(firstTen);
//        System.out.println(jsonObject);
//        return jsonObject;
    }
}
