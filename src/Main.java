import java.util.Scanner;

/**
 * Main class - CLI interface for AI-Powered To-Do Manager
 * Provides interactive command-line interface for task management
 */
public class Main {
    private static final String APP_NAME = "🧠 AI-Powered To-Do Manager";
    private static final String VERSION = "v1.0.0";
    private static final String AUTHOR = "Saurabh";
    private static final String DOMAIN = "todo.saurabhh.in";
    
    private static TaskManager taskManager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        // Initialize components
        taskManager = new TaskManager();
        scanner = new Scanner(System.in);
        
        // Display welcome message
        displayWelcome();
        
        // Main application loop
        boolean running = true;
        while (running) {
            try {
                displayMenu();
                int choice = getUserChoice();
                running = handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("❌ An error occurred: " + e.getMessage());
                System.out.println("🔄 Please try again.");
            }
        }
        
        // Cleanup and exit
        displayGoodbye();
        scanner.close();
    }
    
    /**
     * Displays welcome message and app info
     */
    private static void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎉 Welcome to " + APP_NAME + " " + VERSION);
        System.out.println("👨‍💻 Created by: " + AUTHOR);
        System.out.println("🌐 Live at: " + DOMAIN);
        System.out.println("=".repeat(60));
        System.out.println("\n🚀 Smart task management with AI-powered categorization!");
        System.out.println("💡 Tasks are automatically categorized as Work, Personal, or Urgent");
        System.out.println("📁 All data is saved locally in tasks.json");
        
        // Show initial task summary if tasks exist
        if (taskManager.getTaskCount() > 0) {
            taskManager.printTaskSummary();
        }
    }
    
    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("📋 MAIN MENU");
        System.out.println("-".repeat(40));
        System.out.println("1. ➕ Add New Task");
        System.out.println("2. 📝 Add Task with Manual Category");
        System.out.println("3. 👀 View All Tasks");
        System.out.println("4. 📂 View Tasks by Category");
        System.out.println("5. ⏳ View Pending Tasks");
        System.out.println("6. ✅ View Completed Tasks");
        System.out.println("7. ✔️  Mark Task as Complete");
        System.out.println("8. 🗑️  Delete Task");
        System.out.println("9. 🔍 Search Tasks");
        System.out.println("10. 📊 Task Summary");
        System.out.println("11. 🧹 Clear Completed Tasks");
        System.out.println("12. ❓ Help");
        System.out.println("0. 🚪 Exit");
        System.out.println("-".repeat(40));
        System.out.print("👉 Enter your choice (0-12): ");
    }
    
    /**
     * Gets user choice from input
     * 
     * @return The user's menu choice
     */
    private static int getUserChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number between 0-12.");
            return -1;
        }
    }
    
    /**
     * Handles the user's menu choice
     * 
     * @param choice The menu choice
     * @return true to continue, false to exit
     */
    private static boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addNewTask();
                break;
            case 2:
                addTaskWithManualCategory();
                break;
            case 3:
                taskManager.viewAllTasks();
                break;
            case 4:
                viewTasksByCategory();
                break;
            case 5:
                taskManager.viewPendingTasks();
                break;
            case 6:
                taskManager.viewCompletedTasks();
                break;
            case 7:
                markTaskComplete();
                break;
            case 8:
                deleteTask();
                break;
            case 9:
                searchTasks();
                break;
            case 10:
                taskManager.printTaskSummary();
                break;
            case 11:
                taskManager.clearCompletedTasks();
                break;
            case 12:
                displayHelp();
                break;
            case 0:
                return false; // Exit
            default:
                System.out.println("❌ Invalid choice. Please enter a number between 0-12.");
        }
        
        // Pause before showing menu again
        if (choice != 0) {
            System.out.println("\n⏸️  Press Enter to continue...");
            scanner.nextLine();
        }
        
        return true;
    }
    
    /**
     * Adds a new task with auto-categorization
     */
    private static void addNewTask() {
        System.out.println("\n➕ ADD NEW TASK");
        System.out.println("-".repeat(30));
        System.out.print("📝 Enter task title: ");
        
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("❌ Task title cannot be empty!");
            return;
        }
        
        try {
            taskManager.addTask(title);
        } catch (Exception e) {
            System.out.println("❌ Error adding task: " + e.getMessage());
        }
    }
    
    /**
     * Adds a task with manual category selection
     */
    private static void addTaskWithManualCategory() {
        System.out.println("\n📝 ADD TASK WITH MANUAL CATEGORY");
        System.out.println("-".repeat(40));
        System.out.print("📝 Enter task title: ");
        
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("❌ Task title cannot be empty!");
            return;
        }
        
        // Show available categories
        System.out.println("\n📂 Available Categories:");
        String[] categories = AICategorizer.getAvailableCategories();
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("%d. %s\n", i + 1, categories[i]);
        }
        
        System.out.print("\n👉 Select category (1-" + categories.length + ") or press Enter for auto-categorization: ");
        String categoryInput = scanner.nextLine().trim();
        
        try {
            if (categoryInput.isEmpty()) {
                // Auto-categorize
                taskManager.addTask(title);
            } else {
                int categoryIndex = Integer.parseInt(categoryInput) - 1;
                if (categoryIndex >= 0 && categoryIndex < categories.length) {
                    taskManager.addTask(title, categories[categoryIndex]);
                } else {
                    System.out.println("❌ Invalid category selection. Using auto-categorization.");
                    taskManager.addTask(title);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Using auto-categorization.");
            taskManager.addTask(title);
        } catch (Exception e) {
            System.out.println("❌ Error adding task: " + e.getMessage());
        }
    }
    
    /**
     * Views tasks by selected category
     */
    private static void viewTasksByCategory() {
        System.out.println("\n📂 VIEW TASKS BY CATEGORY");
        System.out.println("-".repeat(35));
        
        String[] categories = AICategorizer.getAvailableCategories();
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("%d. %s\n", i + 1, categories[i]);
        }
        
        System.out.print("\n👉 Select category (1-" + categories.length + "): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= categories.length) {
                taskManager.viewTasksByCategory(categories[choice - 1]);
            } else {
                System.out.println("❌ Invalid category selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
        }
    }
    
    /**
     * Marks a task as complete
     */
    private static void markTaskComplete() {
        if (taskManager.getTaskCount() == 0) {
            System.out.println("\n📭 No tasks available to complete.");
            return;
        }
        
        System.out.println("\n✔️ MARK TASK AS COMPLETE");
        System.out.println("-".repeat(35));
        
        // Show pending tasks
        taskManager.viewPendingTasks();
        
        System.out.print("\n👉 Enter task number to complete: ");
        
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            taskManager.completeTask(taskNumber);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid task number.");
        }
    }
    
    /**
     * Deletes a task
     */
    private static void deleteTask() {
        if (taskManager.getTaskCount() == 0) {
            System.out.println("\n📭 No tasks available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ DELETE TASK");
        System.out.println("-".repeat(20));
        
        // Show all tasks
        taskManager.viewAllTasks();
        
        System.out.print("\n👉 Enter task number to delete: ");
        
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            
            // Confirm deletion
            System.out.print("⚠️  Are you sure you want to delete this task? (y/N): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("y") || confirmation.equals("yes")) {
                taskManager.deleteTask(taskNumber);
            } else {
                System.out.println("❌ Task deletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid task number.");
        }
    }
    
    /**
     * Searches tasks by keyword
     */
    private static void searchTasks() {
        System.out.println("\n🔍 SEARCH TASKS");
        System.out.println("-".repeat(20));
        System.out.print("🔎 Enter search keyword: ");
        
        String keyword = scanner.nextLine().trim();
        
        if (keyword.isEmpty()) {
            System.out.println("❌ Search keyword cannot be empty!");
            return;
        }
        
        taskManager.searchTasks(keyword);
    }
    
    /**
     * Displays help information
     */
    private static void displayHelp() {
        System.out.println("\n❓ HELP - AI-POWERED TO-DO MANAGER");
        System.out.println("=".repeat(50));
        
        System.out.println("\n🧠 AI CATEGORIZATION:");
        System.out.println("Tasks are automatically categorized based on keywords:");
        System.out.println("• 💼 Work: office, project, client, meeting, etc.");
        System.out.println("• 👤 Personal: family, shopping, home, health, etc.");
        System.out.println("• 🚨 Urgent: urgent, asap, today, emergency, etc.");
        System.out.println("• 📝 General: default category for unmatched tasks");
        
        System.out.println("\n📝 TIPS FOR BETTER CATEGORIZATION:");
        System.out.println("• Include specific keywords in your task titles");
        System.out.println("• Use words like 'urgent' or 'today' for time-sensitive tasks");
        System.out.println("• Mention 'work', 'office', or 'client' for professional tasks");
        System.out.println("• Add 'family', 'personal', or 'home' for personal tasks");
        
        System.out.println("\n💾 DATA STORAGE:");
        System.out.println("• All tasks are saved in 'tasks.json' file");
        System.out.println("• Data persists between application sessions");
        System.out.println("• Backup your tasks.json file regularly");
        
        System.out.println("\n🌐 DEPLOYMENT:");
        System.out.println("• This app can be deployed to: " + DOMAIN);
        System.out.println("• Use 'mvn clean package' to create deployable JAR");
        System.out.println("• Run with 'java -jar ai-todo-manager.jar'");
        
        System.out.println("\n🆘 SUPPORT:");
        System.out.println("• Created by: " + AUTHOR);
        System.out.println("• Version: " + VERSION);
        System.out.println("• For issues, check the README.md file");
    }
    
    /**
     * Displays goodbye message
     */
    private static void displayGoodbye() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("👋 Thank you for using " + APP_NAME + "!");
        System.out.println("🎯 Stay productive and organized!");
        System.out.println("🌐 Visit us at: " + DOMAIN);
        System.out.println("=".repeat(50));
        
        // Show final summary
        if (taskManager.getTaskCount() > 0) {
            System.out.println("\n📊 Final Summary:");
            taskManager.printTaskSummary();
        }
        
        System.out.println("\n💾 All tasks have been saved to tasks.json");
        System.out.println("🔄 Run the application again to continue managing your tasks!");
    }
}