import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TaskManager handles all task operations including CRUD, file I/O, and filtering
 */
public class TaskManager {
    private static final String TASKS_FILE = "tasks.json";
    private List<Task> tasks;
    private Gson gson;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadTasks();
    }
    
    /**
     * Adds a new task with auto-categorization
     * 
     * @param title The task title
     * @return The created task
     */
    public Task addTask(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        
        String category = AICategorizer.categorizeTask(title);
        Task task = new Task(title.trim(), category);
        tasks.add(task);
        saveTasks();
        
        System.out.println("\n✅ Task added successfully!");
        System.out.println("📝 Title: " + task.getTitle());
        System.out.println("🏷️  Category: " + task.getCategoryEmoji() + " " + task.getCategory());
        System.out.println("🎯 Confidence: " + String.format("%.0f%%", AICategorizer.getConfidenceScore(title, category) * 100));
        System.out.println(AICategorizer.getSuggestion(title));
        
        return task;
    }
    
    /**
     * Adds a task with manual category override
     * 
     * @param title The task title
     * @param category The manual category
     * @return The created task
     */
    public Task addTask(String title, String category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        
        if (!isValidCategory(category)) {
            category = AICategorizer.categorizeTask(title);
            System.out.println("⚠️  Invalid category provided. Auto-categorized as: " + category);
        }
        
        Task task = new Task(title.trim(), category);
        tasks.add(task);
        saveTasks();
        
        System.out.println("\n✅ Task added with manual category!");
        System.out.println("📝 Title: " + task.getTitle());
        System.out.println("🏷️  Category: " + task.getCategoryEmoji() + " " + task.getCategory());
        
        return task;
    }
    
    /**
     * Views all tasks with optional filtering
     */
    public void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\n📭 No tasks found. Add some tasks to get started!");
            return;
        }
        
        System.out.println("\n📋 ALL TASKS");
        System.out.println("=" .repeat(50));
        
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, tasks.get(i).toString());
        }
        
        printTaskSummary();
    }
    
    /**
     * Views tasks by category
     * 
     * @param category The category to filter by
     */
    public void viewTasksByCategory(String category) {
        List<Task> filteredTasks = tasks.stream()
            .filter(task -> task.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
        
        if (filteredTasks.isEmpty()) {
            System.out.println("\n📭 No tasks found in category: " + category);
            return;
        }
        
        System.out.println("\n📋 TASKS IN CATEGORY: " + category.toUpperCase());
        System.out.println("=" .repeat(50));
        
        for (int i = 0; i < filteredTasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, filteredTasks.get(i).toString());
        }
    }
    
    /**
     * Views only pending tasks
     */
    public void viewPendingTasks() {
        List<Task> pendingTasks = tasks.stream()
            .filter(task -> !task.isCompleted())
            .collect(Collectors.toList());
        
        if (pendingTasks.isEmpty()) {
            System.out.println("\n🎉 Congratulations! No pending tasks. You're all caught up!");
            return;
        }
        
        System.out.println("\n⏳ PENDING TASKS");
        System.out.println("=" .repeat(50));
        
        for (int i = 0; i < pendingTasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, pendingTasks.get(i).toString());
        }
    }
    
    /**
     * Views only completed tasks
     */
    public void viewCompletedTasks() {
        List<Task> completedTasks = tasks.stream()
            .filter(Task::isCompleted)
            .collect(Collectors.toList());
        
        if (completedTasks.isEmpty()) {
            System.out.println("\n📭 No completed tasks yet. Complete some tasks to see them here!");
            return;
        }
        
        System.out.println("\n✅ COMPLETED TASKS");
        System.out.println("=" .repeat(50));
        
        for (int i = 0; i < completedTasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, completedTasks.get(i).toString());
        }
    }
    
    /**
     * Marks a task as complete by index
     * 
     * @param index The task index (1-based)
     * @return true if successful, false otherwise
     */
    public boolean completeTask(int index) {
        if (index < 1 || index > tasks.size()) {
            System.out.println("❌ Invalid task number. Please enter a number between 1 and " + tasks.size());
            return false;
        }
        
        Task task = tasks.get(index - 1);
        if (task.isCompleted()) {
            System.out.println("ℹ️  Task is already completed: " + task.getTitle());
            return false;
        }
        
        task.markComplete();
        saveTasks();
        
        System.out.println("\n🎉 Task completed successfully!");
        System.out.println("✅ " + task.getTitle());
        
        return true;
    }
    
    /**
     * Deletes a task by index
     * 
     * @param index The task index (1-based)
     * @return true if successful, false otherwise
     */
    public boolean deleteTask(int index) {
        if (index < 1 || index > tasks.size()) {
            System.out.println("❌ Invalid task number. Please enter a number between 1 and " + tasks.size());
            return false;
        }
        
        Task task = tasks.get(index - 1);
        tasks.remove(index - 1);
        saveTasks();
        
        System.out.println("\n🗑️  Task deleted successfully!");
        System.out.println("❌ " + task.getTitle());
        
        return true;
    }
    
    /**
     * Searches tasks by keyword
     * 
     * @param keyword The search keyword
     */
    public void searchTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("❌ Search keyword cannot be empty");
            return;
        }
        
        List<Task> matchingTasks = tasks.stream()
            .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
        
        if (matchingTasks.isEmpty()) {
            System.out.println("\n🔍 No tasks found matching: " + keyword);
            return;
        }
        
        System.out.println("\n🔍 SEARCH RESULTS FOR: " + keyword);
        System.out.println("=" .repeat(50));
        
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, matchingTasks.get(i).toString());
        }
    }
    
    /**
     * Prints task summary statistics
     */
    public void printTaskSummary() {
        int total = tasks.size();
        int completed = (int) tasks.stream().filter(Task::isCompleted).count();
        int pending = total - completed;
        
        Map<String, Long> categoryCount = tasks.stream()
            .collect(Collectors.groupingBy(Task::getCategory, Collectors.counting()));
        
        System.out.println("\n📊 TASK SUMMARY");
        System.out.println("-" .repeat(30));
        System.out.println("📝 Total Tasks: " + total);
        System.out.println("✅ Completed: " + completed);
        System.out.println("⏳ Pending: " + pending);
        
        if (!categoryCount.isEmpty()) {
            System.out.println("\n📂 By Category:");
            categoryCount.forEach((category, count) -> {
                String emoji = getEmojiForCategory(category);
                System.out.println("   " + emoji + " " + category + ": " + count);
            });
        }
        
        if (total > 0) {
            double completionRate = (double) completed / total * 100;
            System.out.printf("\n🎯 Completion Rate: %.1f%%\n", completionRate);
        }
    }
    
    /**
     * Loads tasks from JSON file
     */
    private void loadTasks() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            System.out.println("📁 Creating new tasks file: " + TASKS_FILE);
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Type taskListType = new TypeToken<List<Task>>(){}.getType();
            List<Task> loadedTasks = gson.fromJson(reader, taskListType);
            
            if (loadedTasks != null) {
                this.tasks = loadedTasks;
                System.out.println("📂 Loaded " + tasks.size() + " tasks from " + TASKS_FILE);
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading tasks: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error parsing tasks file: " + e.getMessage());
            System.out.println("🔄 Starting with empty task list");
        }
    }
    
    /**
     * Saves tasks to JSON file
     */
    private void saveTasks() {
        try (FileWriter writer = new FileWriter(TASKS_FILE)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("❌ Error saving tasks: " + e.getMessage());
        }
    }
    
    /**
     * Validates if a category is valid
     * 
     * @param category The category to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidCategory(String category) {
        String[] validCategories = AICategorizer.getAvailableCategories();
        return Arrays.stream(validCategories)
            .anyMatch(cat -> cat.equalsIgnoreCase(category));
    }
    
    /**
     * Gets emoji for a category
     * 
     * @param category The category name
     * @return The corresponding emoji
     */
    private String getEmojiForCategory(String category) {
        switch (category.toLowerCase()) {
            case "work": return "💼";
            case "personal": return "👤";
            case "urgent": return "🚨";
            default: return "📝";
        }
    }
    
    /**
     * Gets the total number of tasks
     * 
     * @return The total task count
     */
    public int getTaskCount() {
        return tasks.size();
    }
    
    /**
     * Gets the list of all tasks (read-only)
     * 
     * @return Unmodifiable list of tasks
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
    
    /**
     * Clears all completed tasks
     * 
     * @return Number of tasks cleared
     */
    public int clearCompletedTasks() {
        int initialSize = tasks.size();
        tasks.removeIf(Task::isCompleted);
        int removedCount = initialSize - tasks.size();
        
        if (removedCount > 0) {
            saveTasks();
            System.out.println("\n🧹 Cleared " + removedCount + " completed task(s)");
        } else {
            System.out.println("\n📭 No completed tasks to clear");
        }
        
        return removedCount;
    }
}