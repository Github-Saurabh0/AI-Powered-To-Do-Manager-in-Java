# 🧠 AI-Powered To-Do Manager in Java ✅

Smart CLI-based task manager that auto-categorizes your tasks using intelligent keyword-based logic. No paid API needed - completely free and runs locally!

## 🌟 Features

- ➕ **Add Tasks**: Create new tasks with intelligent auto-categorization
- 🏷️ **Smart Categories**: Automatic categorization into Work, Personal, Urgent, or General
- 👀 **View & Filter**: View all tasks, filter by category, status, or search by keywords
- ✅ **Task Management**: Mark tasks as complete, delete tasks, clear completed tasks
- 💾 **Persistent Storage**: All data saved locally in JSON format
- 📊 **Analytics**: Task summary with completion rates and category breakdown
- 🔍 **Search**: Find tasks quickly using keyword search
- 🎯 **Confidence Scoring**: See how confident the AI is about categorization
- 🌐 **Deployment Ready**: Can be hosted on subdomain (todo.saurabhh.in)

## 🧠 AI Categorization Logic

Tasks are automatically categorized based on keyword analysis:

### 💼 Work Category
Keywords: `office`, `project`, `client`, `meeting`, `presentation`, `report`, `deadline`, `email`, `call`, `conference`, `proposal`, `budget`, `team`, `manager`, `work`, `job`, `business`, `professional`, `corporate`, `company`, `task`, `assignment`, `review`, `analysis`, `development`, `coding`, `programming`, `software`, `application`, `system`, `database`, `server`

### 👤 Personal Category
Keywords: `birthday`, `family`, `shopping`, `grocery`, `home`, `house`, `personal`, `friend`, `vacation`, `holiday`, `doctor`, `appointment`, `health`, `exercise`, `gym`, `hobby`, `book`, `movie`, `restaurant`, `dinner`, `lunch`, `breakfast`, `party`, `celebration`, `gift`, `anniversary`, `wedding`, `travel`, `trip`, `visit`, `clean`, `organize`, `repair`, `maintenance`, `garden`, `pet`, `car`, `insurance`, `bank`, `finance`

### 🚨 Urgent Category
Keywords: `urgent`, `asap`, `immediately`, `now`, `today`, `emergency`, `critical`, `important`, `priority`, `rush`, `quick`, `fast`, `soon`, `deadline`, `overdue`, `late`, `must`, `need`, `required`, `essential`, `crucial`, `vital`, `pressing`, `time-sensitive`, `hurry`, `instant`, `immediate`

### 📝 General Category
Default category for tasks that don't match any specific keywords.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/AI-Powered-To-Do-Manager-in-Java.git
   cd AI-Powered-To-Do-Manager-in-Java
   ```

2. **Compile the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   java -cp target/classes Main
   ```

   Or using Maven:
   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```

4. **Build JAR for deployment**
   ```bash
   mvn clean package
   java -jar target/ai-todo-manager.jar
   ```

## 📁 Project Structure

```
ai-todo-manager/
├── src/
│   ├── Main.java              # CLI interface and main application
│   ├── Task.java              # Task data model
│   ├── TaskManager.java       # Task operations and file I/O
│   └── AICategorizer.java     # AI categorization logic
├── tasks.json                 # Local task storage (auto-created)
├── pom.xml                    # Maven configuration
├── README.md                  # This file
├── LICENSE                    # MIT License
└── .gitignore                # Git ignore rules
```

## 💻 Usage Examples

### Adding Tasks
```
👉 Enter task title: Submit client proposal by Friday
✅ Task added successfully!
📝 Title: Submit client proposal by Friday
🏷️  Category: 💼 Work
🎯 Confidence: 85%
👍 Good categorization! Consider adding more specific keywords for better accuracy.
```

### Viewing Tasks
```
📋 ALL TASKS
==================================================
1. ⏳ Submit client proposal [💼Work] - PENDING (Created: 2024-01-15 09:30:00)
2. ✅ Buy birthday gift for mom [👤Personal] - COMPLETED (Created: 2024-01-15 10:15:00) (Completed: 2024-01-15 18:30:00)
3. ⏳ Urgent: Fix server issue ASAP [🚨Urgent] - PENDING (Created: 2024-01-15 08:00:00)
```

## 🌐 Deployment to Subdomain (todo.saurabhh.in)

### Step 1: Package the Application
```bash
mvn clean package
# Output: target/ai-todo-manager.jar
```

### Step 2: Deploy to Server
```bash
# Upload to server
scp target/ai-todo-manager.jar root@your_server_ip:/home/deploy/

# SSH to server and run
ssh root@your_server_ip
cd /home/deploy/
java -jar ai-todo-manager.jar
```

### Step 3: Apache Reverse Proxy Configuration

Create `/etc/apache2/sites-available/todo.saurabhh.in.conf`:

```apache
<VirtualHost *:80>
    ServerName todo.saurabhh.in
    DocumentRoot /var/www/todo

    ProxyPreserveHost On
    ProxyPass / http://localhost:8080/
    ProxyPassReverse / http://localhost:8080/

    ErrorLog ${APACHE_LOG_DIR}/todo_error.log
    CustomLog ${APACHE_LOG_DIR}/todo_access.log combined
</VirtualHost>
```

### Step 4: Enable Site and Modules
```bash
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2ensite todo.saurabhh.in
sudo systemctl restart apache2
```

### Step 5: DNS Configuration

Add A Record in your domain provider (Hostinger):
- **Name**: `todo`
- **Type**: `A`
- **Value**: `your_server_ip`
- **TTL**: `3600`

## 📊 Sample tasks.json Format

```json
[
  {
    "title": "Submit client proposal",
    "category": "Work",
    "completed": false,
    "createdAt": "2025-01-15 09:30:00",
    "completedAt": null
  },
  {
    "title": "Buy birthday gift for mom",
    "category": "Personal",
    "completed": true,
    "createdAt": "2025-01-15 10:15:00",
    "completedAt": "2025-01-15 18:30:00"
  }
]
```

## 🛠️ Technologies Used

- **Java 17+**: Core programming language
- **Maven**: Dependency management and build tool
- **Gson**: JSON parsing and serialization
- **CLI Interface**: Interactive command-line interface
- **Apache/Nginx**: Web server for deployment
- **JSON**: Local data storage format

## 🎯 Development Roadmap

### Day 1 ✅
- [x] Project setup and Maven configuration
- [x] Core classes: Task, TaskManager, AICategorizer, Main
- [x] Basic CRUD operations
- [x] JSON file storage
- [x] CLI interface

### Day 2 (Planned)
- [ ] Enhanced UI/UX with better formatting
- [ ] Advanced search and filtering
- [ ] Task priority levels
- [ ] Due date functionality

### Day 3 (Planned)
- [ ] Web interface (Spring Boot)
- [ ] REST API endpoints
- [ ] Task statistics and analytics
- [ ] Export/Import functionality

### Day 4 (Planned)
- [ ] Deployment optimization
- [ ] Performance improvements
- [ ] Documentation completion
- [ ] Testing and bug fixes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Saurabh**
- 🌐 Website: [saurabhh.in](https://saurabhh.in)
- 📧 Email: contact@saurabhh.in
- 🚀 Live Demo: [todo.saurabhh.in](https://todo.saurabhh.in)

## 🙏 Acknowledgments

- Thanks to the Java community for excellent documentation
- Gson library for seamless JSON handling
- Maven for simplified dependency management
- All contributors and users of this project

---

⭐ **Star this repository if you find it helpful!** ⭐
A smart Java-based task manager that uses OpenAI GPT to auto-categorize your tasks (Work, Personal, Urgent). Saves all data locally in JSON. CLI-based, deployable on custom subdomain.
