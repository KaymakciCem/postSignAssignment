package com.signavio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    private static final int NUMBER_OF_THREADS = 5;
    private final ExecutorService executorServiceGrouping = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final ExecutorService executorServiceStatistics = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static void main(String[] args) {
        List<EventlogRow> eventlogRows = CSVReader.readFile("samples/Activity_Log_2004_to_2014.csv");

        long begin = System.currentTimeMillis();

        // TODO: Add the call to your solution here
        new App().execute(eventlogRows);

        long end = System.currentTimeMillis();
        System.out.printf("Duration: %s milliseconds", end - begin);
    }

    private void execute(final List<EventlogRow> eventlogRows) {
        createGroups(eventlogRows);
        calculateStatistics();
        ProcureToPayActivity.print();
    }

    private void createGroups(List<EventlogRow> eventlogRows) {
        int rowCount = eventlogRows.size();
        System.out.println(Thread.currentThread().getName() + ". thread icerisindeyz");
        List<Callable<String>> groupingTasks = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int startIndex = i * (rowCount / NUMBER_OF_THREADS) + i;
            int endIndex = startIndex + (rowCount / NUMBER_OF_THREADS);

            GroupingLogs groupingLogs = new GroupingLogs(startIndex, Math.min(endIndex, rowCount), eventlogRows);
            groupingTasks.add(groupingLogs);
        }

        try {
            executorServiceGrouping.invokeAny(groupingTasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorServiceGrouping.shutdown();
    }

    private void calculateStatistics() {
        System.out.println(Thread.currentThread().getName() + ". thread icerisindeyz");

        Map<String, TreeSet<EventlogRow>> recordTreeSet = ProcureToPayActivity.getActivitiesMap();

        recordTreeSet.keySet().forEach(key -> executorServiceStatistics
                .execute(() -> ProcureToPayActivity.createStatistics(key)));

        executorServiceStatistics.shutdown();
        try {
            executorServiceStatistics.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class GroupingLogs implements Callable {
        private int startIndex;
        private int endIndex;
        private List<EventlogRow> eventlogRows;

        public GroupingLogs(int startIndex, int endIndex, List<EventlogRow> eventlogRows) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.eventlogRows = eventlogRows;
        }

        @Override
        public Object call() {
            ProcureToPayActivity.groupActivitiesTreeSet(startIndex, endIndex, eventlogRows);
            return "";
        }
    }
}