package org.example;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounterEveryCounterPersistence {
    private static final AtomicInteger counter = new AtomicInteger();
    private static final String COUNTER_FILE = "counter.txt";

    public static void main(String[] args) {
        // Load the counter value from disk on startup
        loadCounterFromFile();

        // Increment the counter and persist the value on disk
        for (int i = 0; i < 10; i++) {
            int newValue = incrementAndPersist();
            System.out.println("Incremented Value: " + newValue);
        }
    }

    // Method to atomically increment the counter and persist the new value to a file
    public static int incrementAndPersist() {
        // Atomically increment the counter and get the updated value
        int newValue = counter.incrementAndGet();

        // Persist the updated value to disk
        persistCounterToFile(newValue);

        return newValue;
    }

    // Load the counter value from the file, if available
    private static void loadCounterFromFile() {
        File file = new File(COUNTER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    counter.set(Integer.parseInt(line)); // Set the counter to the persisted value
                }
            } catch (IOException e) {
                System.err.println("Error reading counter from file: " + e.getMessage());
            }
        }
    }

    // Persist the current counter value to a file
    private static void persistCounterToFile(int value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.err.println("Error writing counter to file: " + e.getMessage());
        }
    }
}
