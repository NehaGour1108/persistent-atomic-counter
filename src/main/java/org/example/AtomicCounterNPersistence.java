package org.example;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounterNPersistence {

    private static final String COUNTER_FILE = "counter.txt";  // File to persist the counter
    private static AtomicInteger counter = new AtomicInteger(0);  // Atomic counter
    private static final int PERSIST_INTERVAL = 10;  // Persist every n iterations (e.g., every 10 increments)
    private static int iterations = 0;  // Track iterations

    public static void main(String[] args) {
        // Initialize the counter from the file if it exists
        initializeCounter();

        // Simulate atomic increments with persistence every n iterations
        for (int i = 0; i < 100; i++) {
            incrementAndPersistIfNeeded();
        }
    }

    // Method to initialize the counter from the file if it exists
    private static void initializeCounter() {
        File file = new File(COUNTER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null && line.matches("\\d+")) {  // Ensure it's a valid integer
                    counter.set(Integer.parseInt(line));
                    System.out.println("Counter initialized from file: " + counter.get());
                }
            } catch (IOException e) {
                System.err.println("Error reading the counter file: " + e.getMessage());
            }
        } else {
            System.out.println("No counter file found. Starting fresh.");
        }
    }

    // Method to increment the counter and persist if needed
    private static void incrementAndPersistIfNeeded() {
        // Increment the counter atomically
        int currentValue = counter.incrementAndGet();

        // Increment the iteration count
        iterations++;

        // Print the counter value for this iteration (optional)
        System.out.println("Iteration: " + iterations + " Counter: " + currentValue);

        // Persist to file every n iterations
        if (iterations % PERSIST_INTERVAL == 0) {
            persistCounterToFile(currentValue);
        }
    }

    // Method to persist the counter to a file
    private synchronized static void persistCounterToFile(int value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(String.valueOf(value));
            writer.flush();  // Ensure data is written immediately
            System.out.println("Counter persisted to file: " + value);
        } catch (IOException e) {
            System.err.println("Error writing counter to file: " + e.getMessage());
        }
    }
}
