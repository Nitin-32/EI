import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Task {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private boolean completed;

    public Task(String description, LocalTime startTime, LocalTime endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public void markCompleted() { this.completed = true; }

    @Override
    public String toString() {
        return startTime + " - " + endTime + ": " + description +
                " [" + priority + "]" + (completed ? " (Completed)" : "");
    }
}

class TaskFactory {
    public static Task createTask(String description, String start, String end, String priority) {
        try {
            LocalTime sTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime eTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm"));

            if (eTime.isBefore(sTime)) {
                throw new IllegalArgumentException("End time cannot be before start time.");
            }
            return new Task(description, sTime, eTime, priority);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error: Invalid time format. Use HH:mm (24-hour).");
        }
    }
}

interface Observer {
    void update(String message);
}

class ConflictNotifier implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[NOTIFICATION] " + message);
    }
}

class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private List<Observer> observers;

    private ScheduleManager() {
        tasks = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    private void notifyObservers(String message) {
        for (Observer obs : observers) {
            obs.update(message);
        }
    }

    public void addTask(Task task) {
        for (Task t : tasks) {
            if (!(task.getEndTime().isBefore(t.getStartTime()) || task.getStartTime().isAfter(t.getEndTime()))) {
                notifyObservers("Task conflicts with existing task \"" + t.getDescription() + "\"");
                return;
            }
        }
        tasks.add(task);
        System.out.println("Task added successfully. No conflicts.");
    }

    public void removeTask(String description) {
        Task found = null;
        for (Task t : tasks) {
            if (t.getDescription().equalsIgnoreCase(description)) {
                found = t;
                break;
            }
        }
        if (found != null) {
            tasks.remove(found);
            System.out.println("Task removed successfully.");
        } else {
            System.out.println("Error: Task not found.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }
        tasks.sort(Comparator.comparing(Task::getStartTime));
        for (Task t : tasks) {
            System.out.println(t);
        }
    }

    public void markTaskCompleted(String description) {
        for (Task t : tasks) {
            if (t.getDescription().equalsIgnoreCase(description)) {
                t.markCompleted();
                System.out.println("Task marked as completed.");
                return;
            }
        }
        System.out.println("Error: Task not found.");
    }

    public void viewTasksByPriority(String priority) {
        boolean found = false;
        for (Task t : tasks) {
            if (t.getPriority().equalsIgnoreCase(priority)) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks with priority: " + priority);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ScheduleManager manager = ScheduleManager.getInstance();
        manager.addObserver(new ConflictNotifier());

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Astronaut Daily Schedule Organizer ---");
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View All Tasks");
            System.out.println("4. Mark Task Completed");
            System.out.println("5. View Tasks by Priority");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String desc = sc.nextLine();
                    System.out.print("Enter start time (HH:mm): ");
                    String start = sc.nextLine();
                    System.out.print("Enter end time (HH:mm): ");
                    String end = sc.nextLine();
                    System.out.print("Enter priority (High/Medium/Low): ");
                    String priority = sc.nextLine();

                    try {
                        Task task = TaskFactory.createTask(desc, start, end, priority);
                        manager.addTask(task);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter description of task to remove: ");
                    String rem = sc.nextLine();
                    manager.removeTask(rem);
                    break;

                case 3:
                    manager.viewTasks();
                    break;

                case 4:
                    System.out.print("Enter description of task to mark completed: ");
                    String comp = sc.nextLine();
                    manager.markTaskCompleted(comp);
                    break;

                case 5:
                    System.out.print("Enter priority to filter (High/Medium/Low): ");
                    String pr = sc.nextLine();
                    manager.viewTasksByPriority(pr);
                    break;

                case 6:
                    System.out.println("Exiting program... Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
