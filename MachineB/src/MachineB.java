import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.*;
import java.io.File;

public class MachineB {
    private static int machineAEndTime;

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);

        String filename = scanner.nextLine();
        File infile = new File(filename);

        try {
            PriorityQueue<Task> taskQueue = getTasks(infile);

            // Counter to keep track of the number of tasks
            int count = 0;

            // Logic for task scheduling
            while (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                if (task.start_time >= machineAEndTime) {
                    machineAEndTime = task.end_time; // Update machine's next free time
                    count++;
                }
            }
            // Output to be tested
            System.out.println(count);
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

        // Priority queue to store tasks comparing by end time (Optimized for MachineB with IntelliJ IDEA's suggestion)
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(Comparator.comparingInt(t -> t.end_time));

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

