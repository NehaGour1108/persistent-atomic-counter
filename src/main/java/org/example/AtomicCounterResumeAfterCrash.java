import java.io.*;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounterResumeAfterCrash {
    private static final String COUNTER_FILE = "counter.dat";  // File to store the counter value
    private static final int PERSIST_INTERVAL = 10;  // Persist every 10 iterations

    private AtomicInteger counter;

    public AtomicCounterResumeAfterCrash() {
        // Initialize counter from last persisted value or start from 0 if no file exists
        this.counter = new AtomicInteger(loadLastPersistedCounter());
    }

    // Load the last persisted counter value from disk
    private int loadLastPersistedCounter() {
        try {
            Path path = Paths.get(COUNTER_FILE);
            if (Files.exists(path)) {
                // Read last persisted counter value
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    return Integer.parseInt(reader.readLine());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;  // Default value if no file or invalid content
    }

    // Persist the current counter value to disk
    private void persistCounter() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(COUNTER_FILE))) {
            writer.write(String.valueOf(counter.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Increment the counter and persist periodically
    public void incrementCounter() {
        int count = counter.incrementAndGet();
        System.out.println("Current Counter: " + count);

        // Persist every PERSIST_INTERVAL increments
        if (count % PERSIST_INTERVAL == 0) {
            persistCounter();
            System.out.println("Counter persisted: " + count);
        }
    }

    // Retrieve the current counter value
    public int getCounterValue() {
        return counter.get();
    }

    public static void main(String[] args) {
        AtomicCounterResumeAfterCrash atomicCounter = new AtomicCounterResumeAfterCrash();

        // Simulating increments
        for (int i = 0; i < 50; i++) {
            atomicCounter.incrementCounter();
            try {
                Thread.sleep(100);  // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check the final value after increments
        System.out.println("Final counter value: " + atomicCounter.getCounterValue());
    }
}
