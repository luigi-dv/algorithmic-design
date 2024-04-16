import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.*;
import java.io.File;

public class MachineA {
    private static int machineAEndTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String filename = scanner.nextLine();
        File infile = new File(filename);

        try {
            PriorityQueue<Task> taskQueue = getTasks(infile);
            // Logic for task scheduling
            while (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                if (task.start_time >= machineAEndTime) {
                    // Output to be tested
                    System.out.printf("%d %d\n", task.start_time, task.end_time);
                    // Updating the end time of our machineA
                    machineAEndTime = task.end_time;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }

    /**
     * Reads tasks from a file and returns a task's priority queue
     * @param infile File containing tasks
     * @return PriorityQueue of tasks
     * @throws FileNotFoundException if file is not found
     */
    private static PriorityQueue<Task> getTasks(File infile) throws IOException {

        // Priority queue to store tasks
        PriorityQueue<Task> taskQueue = new PriorityQueue<>((t1, t2) -> {
            if (t1.start_time != t2.start_time) {
                return Integer.compare(t1.start_time, t2.start_time); // Sort by start time
            } else {
                return Integer.compare(t1.duration, t2.duration); // If start times are equal, sort by duration
            }
        });

        // Reading tasks from file and add them to the priority queue
        try (Scanner input = new Scanner(infile)) {
            int n = input.nextInt();
            for (int i = 0; i < n; i++) {
                int start_time = input.nextInt();
                int end_time = input.nextInt();
                Task task = new Task(start_time, end_time);
                taskQueue.add(task);
            }
        }
        return taskQueue;
    }
}

/**
 * Task Entity that stores task details
 */
class Task {
    public int start_time;
    public int end_time;
    public int duration;

    public Task(int start_time, int end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.duration = end_time - start_time;
    }
}

