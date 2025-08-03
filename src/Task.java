import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Task class represents a single to-do item with title, category, completion status, and timestamp
 */
public class Task {
    private String title;
    private String category;
    private boolean completed;
    private String createdAt;
    private String completedAt;
    
    // Constructor
    public Task(String title, String category) {
        this.title = title;
        this.category = category;
        this.completed = false;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.completedAt = null;
    }
    
    // Default constructor for Gson
    public Task() {}
    
    // Getters
    public String getTitle() {
        return title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getCompletedAt() {
        return completedAt;
    }
    
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (!completed) {
            this.completedAt = null;
        }
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
    
    // Mark task as complete
    public void markComplete() {
        setCompleted(true);
    }
    
    // Mark task as incomplete
    public void markIncomplete() {
        setCompleted(false);
    }
    
    // Get status emoji
    public String getStatusEmoji() {
        return completed ? "✅" : "⏳";
    }
    
    // Get category emoji
    public String getCategoryEmoji() {
        switch (category.toLowerCase()) {
            case "work":
                return "💼";
            case "personal":
                return "👤";
            case "urgent":
                return "🚨";
            default:
                return "📝";
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s %s [%s%s] - %s (Created: %s)%s",
            getStatusEmoji(),
            title,
            getCategoryEmoji(),
            category,
            completed ? "COMPLETED" : "PENDING",
            createdAt,
            completed && completedAt != null ? " (Completed: " + completedAt + ")" : ""
        );
    }
    
    // Simple string representation for display
    public String toSimpleString() {
        return String.format("%s %s [%s]", getStatusEmoji(), title, category);
    }
}