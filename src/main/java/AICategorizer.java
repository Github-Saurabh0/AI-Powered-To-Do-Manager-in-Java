import java.util.Arrays;
import java.util.List;

/**
 * AICategorizer provides rule-based task categorization using keyword matching
 * This simulates AI categorization without requiring external APIs
 */
public class AICategorizer {
    
    // Work-related keywords
    private static final List<String> WORK_KEYWORDS = Arrays.asList(
        "office", "project", "client", "meeting", "presentation", "report", "deadline",
        "email", "call", "conference", "proposal", "budget", "team", "manager",
        "work", "job", "business", "professional", "corporate", "company",
        "task", "assignment", "review", "analysis", "development", "coding",
        "programming", "software", "application", "system", "database", "server",
        "sprint", "jira", "standup", "merge", "deploy", "deployment", "release",
        "pull request", "code review", "pr", "git", "repository", "ticket",
        "stakeholder", "spec", "requirement", "roadmap", "scrum", "agile",
        "invoice", "timesheet", "payroll", "hr", "hrms", "onboarding", "recruitment",
        "zoom", "meet", "teams", "slack", "okr", "kpi"
    );
    
    // Personal-related keywords
    private static final List<String> PERSONAL_KEYWORDS = Arrays.asList(
        "birthday", "family", "shopping", "grocery", "home", "house", "personal",
        "friend", "vacation", "holiday", "doctor", "appointment", "health",
        "exercise", "gym", "hobby", "book", "movie", "restaurant", "dinner",
        "lunch", "breakfast", "party", "celebration", "gift", "anniversary",
        "wedding", "travel", "trip", "visit", "clean", "organize", "repair",
        "maintenance", "garden", "pet", "car", "insurance", "bank", "finance",
        "rent", "electricity", "water bill", "gas bill", "emi", "loan", "bill",
        "milk", "vegetables", "fruits", "medicines", "tuition", "school",
        "college", "exam", "fees", "passport", "aadhaar", "pan", "license",
        "parents", "kids", "spouse", "relatives", "festival", "diwali", "holi"
    );
    
    // Urgent-related keywords
    private static final List<String> URGENT_KEYWORDS = Arrays.asList(
        "urgent", "asap", "immediately", "now", "today", "emergency", "critical",
        "important", "priority", "rush", "quick", "fast", "soon", "deadline",
        "overdue", "late", "must", "need", "required", "essential", "crucial",
        "vital", "pressing", "time-sensitive", "hurry", "instant", "immediate",
        "blocker", "p0", "p1", "hotfix", "sev1", "sev2", "outage", "downtime",
        "escalation", "last date", "last day", "final", "final call"
    );
    
    /**
     * Categorizes a task based on its title using keyword matching
     * Priority: Urgent > Work > Personal > General
     * 
     * @param taskTitle The title of the task to categorize
     * @return The category ("Urgent", "Work", "Personal", or "General")
     */
    public static String categorizeTask(String taskTitle) {
        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            return "General";
        }
        
        String lowerTitle = taskTitle.toLowerCase();
        
        // Check for urgent keywords first (highest priority)
        for (String keyword : URGENT_KEYWORDS) {
            if (lowerTitle.contains(keyword)) {
                return "Urgent";
            }
        }
        
        // Check for work keywords
        for (String keyword : WORK_KEYWORDS) {
            if (lowerTitle.contains(keyword)) {
                return "Work";
            }
        }
        
        // Check for personal keywords
        for (String keyword : PERSONAL_KEYWORDS) {
            if (lowerTitle.contains(keyword)) {
                return "Personal";
            }
        }
        
        // Default category if no keywords match
        return "General";
    }
    
    /**
     * Gets a confidence score for the categorization (0.0 to 1.0)
     * Higher score means more confident in the categorization
     * 
     * @param taskTitle The title of the task
     * @param category The assigned category
     * @return Confidence score between 0.0 and 1.0
     */
    public static double getConfidenceScore(String taskTitle, String category) {
        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            return 0.1;
        }
        
        String lowerTitle = taskTitle.toLowerCase();
        int matchCount = 0;
        List<String> relevantKeywords;
        
        switch (category.toLowerCase()) {
            case "urgent":
                relevantKeywords = URGENT_KEYWORDS;
                break;
            case "work":
                relevantKeywords = WORK_KEYWORDS;
                break;
            case "personal":
                relevantKeywords = PERSONAL_KEYWORDS;
                break;
            default:
                return 0.3; // Low confidence for general category
        }
        
        // Count matching keywords
        for (String keyword : relevantKeywords) {
            if (lowerTitle.contains(keyword)) {
                matchCount++;
            }
        }
        
        // Calculate confidence based on matches
        if (matchCount == 0) {
            return 0.3;
        } else if (matchCount == 1) {
            return 0.7;
        } else if (matchCount == 2) {
            return 0.85;
        } else {
            return 0.95;
        }
    }
    
    /**
     * Provides suggestions for improving task categorization
     * 
     * @param taskTitle The title of the task
     * @return Suggestion string
     */
    public static String getSuggestion(String taskTitle) {
        String category = categorizeTask(taskTitle);
        double confidence = getConfidenceScore(taskTitle, category);
        
        if (confidence < 0.5) {
            return "💡 Tip: Add keywords like 'urgent', 'work', or 'personal' to improve auto-categorization!";
        } else if (confidence < 0.8) {
            return "👍 Good categorization! Consider adding more specific keywords for better accuracy.";
        } else {
            return "🎯 Excellent! Task categorized with high confidence.";
        }
    }
    
    /**
     * Gets all available categories
     * 
     * @return Array of category names
     */
    public static String[] getAvailableCategories() {
        return new String[]{"Work", "Personal", "Urgent", "General"};
    }
}
