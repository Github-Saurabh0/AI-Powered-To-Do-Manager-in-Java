import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class WebServer {
  private final TaskManager taskManager;
  private final int port;
  private HttpServer server;

  public WebServer(TaskManager taskManager, int port) throws IOException {
    this.taskManager = taskManager;
    this.port = port;
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    registerRoutes();
  }

  private void registerRoutes() {
    server.createContext("/", this::handleIndex);
    server.createContext("/api/tasks", this::handleTasks);
    server.createContext("/api/tasks/", this::handleTaskByIndex);
    server.createContext("/favicon.ico", exchange -> {
    try {
        java.nio.file.Path path = java.nio.file.Paths.get("S_logo_my.png"); // Using S_logo_my.png as favicon
        byte[] bytes = java.nio.file.Files.readAllBytes(path);
        exchange.getResponseHeaders().set("Content-Type", "image/png");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    } catch (IOException e) {
        exchange.sendResponseHeaders(404, -1);
    }
});
    server.createContext("/S_logo_my.png", exchange -> {
    try {
        java.nio.file.Path path = java.nio.file.Paths.get("S_logo_my.png"); 
        byte[] bytes = java.nio.file.Files.readAllBytes(path);
        exchange.getResponseHeaders().set("Content-Type", "image/png");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    } catch (IOException e) {
        exchange.sendResponseHeaders(404, -1);
    }
});
  }
  

  public void start() {
    server.start();
    System.out.println("✅ Web server started at http://localhost:" + port + "/");
  }

  private void handleIndex(HttpExchange exchange) throws IOException {
    if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
      sendText(exchange, 405, "Method Not Allowed");
      return;
    }
    String html = "<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><title>AI To-Do Manager</title><link rel=\"icon\" href=\"/favicon.ico\" type=\"image/x-icon\"><link rel=\"shortcut icon\" href=\"/favicon.ico\" type=\"image/x-icon\"><style>*{margin:0;padding:0;box-sizing:border-box}body{font-family:'Inter',-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;background:linear-gradient(135deg,#0f0f23 0%,#1a1a2e 50%,#16213e 100%);color:#e2e8f0;min-height:100vh;padding:20px;overflow-x:hidden}.container{max-width:800px;margin:0 auto;background:rgba(30,41,59,0.8);backdrop-filter:blur(20px);border-radius:24px;border:1px solid rgba(148,163,184,0.1);padding:32px;box-shadow:0 25px 50px -12px rgba(0,0,0,0.5)}.header{text-align:center;margin-bottom:40px}.title{font-size:2.5rem;font-weight:700;background:linear-gradient(135deg,#60a5fa 0%,#a78bfa 50%,#f472b6 100%);-webkit-background-clip:text;-webkit-text-fill-color:transparent;background-clip:text;margin-bottom:8px;text-shadow:0 0 30px rgba(96,165,250,0.3)}.subtitle{color:#94a3b8;font-size:1.1rem;font-weight:400}.input-section{margin-bottom:32px}.input-row{display:flex;gap:12px;margin-bottom:16px}.task-input{flex:1;padding:16px 20px;background:rgba(51,65,85,0.6);border:2px solid rgba(148,163,184,0.2);border-radius:16px;color:#e2e8f0;font-size:16px;transition:all 0.3s ease;backdrop-filter:blur(10px)}.task-input:focus{outline:none;border-color:#60a5fa;box-shadow:0 0 0 4px rgba(96,165,250,0.1),0 0 20px rgba(96,165,250,0.2)}.task-input::placeholder{color:#64748b}.add-btn{padding:16px 32px;background:linear-gradient(135deg,#3b82f6 0%,#8b5cf6 100%);border:none;border-radius:16px;color:white;font-weight:600;font-size:16px;cursor:pointer;transition:all 0.3s ease;box-shadow:0 8px 25px rgba(59,130,246,0.3)}.add-btn:hover:not(:disabled){transform:translateY(-2px);box-shadow:0 12px 35px rgba(59,130,246,0.4)}.add-btn:disabled{opacity:0.6;cursor:not-allowed;transform:none}.error{color:#f87171;font-size:14px;margin-top:8px;padding:8px 16px;background:rgba(239,68,68,0.1);border-radius:8px;border-left:4px solid #ef4444}.filters-section{margin-bottom:32px;padding:24px;background:rgba(51,65,85,0.3);border-radius:20px;border:1px solid rgba(148,163,184,0.1)}.filter-header{display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:16px}.filter-buttons{display:flex;gap:8px}.filter-btn{padding:10px 20px;background:rgba(71,85,105,0.5);border:1px solid rgba(148,163,184,0.2);border-radius:12px;color:#cbd5e1;font-weight:500;cursor:pointer;transition:all 0.3s ease}.filter-btn:hover{background:rgba(96,165,250,0.2);border-color:#60a5fa;color:#60a5fa}.filter-btn.active{background:linear-gradient(135deg,#60a5fa 0%,#8b5cf6 100%);border-color:transparent;color:white;box-shadow:0 4px 15px rgba(96,165,250,0.3)}.counts{display:flex;gap:12px;flex-wrap:wrap}.count-badge{padding:8px 16px;background:rgba(71,85,105,0.4);border-radius:12px;font-size:14px;font-weight:500;border:1px solid rgba(148,163,184,0.1)}.count-number{color:#60a5fa;font-weight:700}.refresh-btn{padding:12px 24px;background:rgba(71,85,105,0.5);border:1px solid rgba(148,163,184,0.2);border-radius:12px;color:#cbd5e1;font-weight:500;cursor:pointer;transition:all 0.3s ease;margin-bottom:24px}.refresh-btn:hover{background:rgba(96,165,250,0.2);border-color:#60a5fa;color:#60a5fa}.tasks-list{list-style:none}.task-item{background:rgba(51,65,85,0.4);border:1px solid rgba(148,163,184,0.1);border-radius:16px;padding:20px;margin-bottom:12px;display:flex;align-items:center;justify-content:space-between;transition:all 0.3s ease;backdrop-filter:blur(10px)}.task-item:hover{background:rgba(51,65,85,0.6);border-color:rgba(148,163,184,0.3);transform:translateY(-1px);box-shadow:0 8px 25px rgba(0,0,0,0.2)}.task-content{flex:1;display:flex;align-items:center;gap:12px}.task-text{font-size:16px;font-weight:500}.task-category{background:rgba(96,165,250,0.2);color:#60a5fa;padding:4px 12px;border-radius:8px;font-size:12px;font-weight:600}.task-completed{color:#10b981}.task-actions{display:flex;gap:8px}.action-btn{padding:8px 16px;border:none;border-radius:10px;font-weight:500;font-size:14px;cursor:pointer;transition:all 0.3s ease}.complete-btn{background:rgba(16,185,129,0.2);color:#10b981;border:1px solid rgba(16,185,129,0.3)}.complete-btn:hover:not(:disabled){background:rgba(16,185,129,0.3);box-shadow:0 4px 15px rgba(16,185,129,0.2)}.complete-btn:disabled{opacity:0.5;cursor:not-allowed}.delete-btn{background:rgba(239,68,68,0.2);color:#ef4444;border:1px solid rgba(239,68,68,0.3)}.delete-btn:hover{background:rgba(239,68,68,0.3);box-shadow:0 4px 15px rgba(239,68,68,0.2)}@keyframes fadeIn{from{opacity:0;transform:translateY(20px)}to{opacity:1;transform:translateY(0)}}.task-item{animation:fadeIn 0.3s ease}@media (max-width:640px){.container{padding:20px;margin:10px}.title{font-size:2rem}.input-row{flex-direction:column}.filter-header{flex-direction:column;align-items:stretch}.counts{justify-content:center}.task-item{flex-direction:column;align-items:stretch;gap:16px}.task-actions{justify-content:center}}</style></head><body><div class=\"container\"><div class=\"header\"><h1 class=\"title\">🤖 AI-Powered To-Do Manager</h1><p class=\"subtitle\">Intelligent task management with modern design</p></div><div class=\"input-section\"><div class=\"input-row\"><input id=\"title\" class=\"task-input\" placeholder=\"What needs to be done?\"/><button id=\"addBtn\" class=\"add-btn\" onclick=\"addTask()\">Add Task</button></div><div id=\"error\" class=\"error\" style=\"display:none\"></div></div><div class=\"filters-section\"><div class=\"filter-header\"><div class=\"filter-buttons\"><button id=\"fAll\" class=\"filter-btn active\" onclick=\"setFilter('all')\">All Tasks</button><button id=\"fPending\" class=\"filter-btn\" onclick=\"setFilter('pending')\">Pending</button><button id=\"fCompleted\" class=\"filter-btn\" onclick=\"setFilter('completed')\">Completed</button></div><div class=\"counts\"><div class=\"count-badge\">Total: <span class=\"count-number\" id=\"cAll\">0</span></div><div class=\"count-badge\">Pending: <span class=\"count-number\" id=\"cPending\">0</span></div><div class=\"count-badge\">Done: <span class=\"count-number\" id=\"cCompleted\">0</span></div></div></div></div><button class=\"refresh-btn\" onclick=\"load()\">🔄 Refresh Tasks</button><ul id=\"list\" class=\"tasks-list\"></ul></div><script>let currentFilter='all';function setFilter(f){currentFilter=f;highlightFilter();render(window.__tasks||[]);}function highlightFilter(){['fAll','fPending','fCompleted'].forEach(id=>{document.getElementById(id).classList.remove('active');});if(currentFilter==='all'){document.getElementById('fAll').classList.add('active');}else if(currentFilter==='pending'){document.getElementById('fPending').classList.add('active');}else{document.getElementById('fCompleted').classList.add('active');}}async function load(){const r=await fetch('/api/tasks');const d=await r.json();window.__tasks=d;render(d);}function computeCounts(tasks){const all=tasks.length;const completed=tasks.filter(t=>t.completed).length;const pending=all-completed;return {all,pending,completed};}function render(tasks){const {all,pending,completed}=computeCounts(tasks);document.getElementById('cAll').textContent=all;document.getElementById('cPending').textContent=pending;document.getElementById('cCompleted').textContent=completed;const ul=document.getElementById('list');ul.innerHTML='';let view=tasks;if(currentFilter==='pending'){view=tasks.filter(t=>!t.completed);}else if(currentFilter==='completed'){view=tasks.filter(t=>t.completed);}view.forEach((t,i)=>{const li=document.createElement('li');li.className='task-item';const content=document.createElement('div');content.className='task-content';const text=document.createElement('span');text.className='task-text';text.textContent=t.title;const category=document.createElement('span');category.className='task-category';category.textContent=t.category;const completed=document.createElement('span');if(t.completed){completed.className='task-completed';completed.textContent='✅ Completed';}content.appendChild(text);content.appendChild(category);if(t.completed)content.appendChild(completed);const actions=document.createElement('div');actions.className='task-actions';const btnC=document.createElement('button');btnC.className='action-btn complete-btn';btnC.textContent='✓ Complete';btnC.disabled=t.completed;btnC.onclick=()=>completeTask(indexFromOriginal(tasks,t));const btnD=document.createElement('button');btnD.className='action-btn delete-btn';btnD.textContent='🗑 Delete';btnD.onclick=()=>deleteTask(indexFromOriginal(tasks,t));actions.appendChild(btnC);actions.appendChild(btnD);li.appendChild(content);li.appendChild(actions);ul.appendChild(li);});highlightFilter();}function indexFromOriginal(allTasks, task){const idx=allTasks.indexOf(task);return (idx>=0?idx:window.__tasks.indexOf(task))+1;}async function addTask(){const input=document.getElementById('title');const btn=document.getElementById('addBtn');const err=document.getElementById('error');err.style.display='none';const v=input.value.trim();if(!v){err.textContent='Please enter a task title';err.style.display='block';return;}btn.disabled=true;btn.textContent='Adding...';try{const r=await fetch('/api/tasks',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({title:v})});if(!r.ok){const e=await r.text();err.textContent='Failed to add task: '+e;err.style.display='block';return;}input.value='';await load();}catch(e){err.textContent='Network error occurred';err.style.display='block';}finally{btn.disabled=false;btn.textContent='Add Task';}}async function completeTask(idx){const r=await fetch('/api/tasks/'+idx+'/complete',{method:'PUT'});if(r.ok){load();}else{const e=await r.text();alert('Failed to complete task: '+e);}}async function deleteTask(idx){const r=await fetch('/api/tasks/'+idx,{method:'DELETE'});if(r.ok){load();}else{const e=await r.text();alert('Failed to delete task: '+e);}}document.getElementById('title').addEventListener('keypress',function(e){if(e.key==='Enter'){addTask();}});load();</script></body></html>";
    sendHtml(exchange, 200, html);
  }

  private void handleTasks(HttpExchange exchange) throws IOException {
    switch (exchange.getRequestMethod()) {
      case "GET":
        List<Task> tasks = getTasksSnapshot();
        String json = toJson(tasks);
        sendJson(exchange, 200, json);
        break;
      case "POST":
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String title = parseTitle(body);
        if (title == null || title.isBlank()) {
          sendJson(exchange, 400, "{\"error\":\"Title is required\"}");
          return;
        }
        try {
          taskManager.addTask(title);
          sendJson(exchange, 201, "{\"status\":\"created\"}");
        } catch (Exception e) {
          sendJson(exchange, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
        break;
      default:
        sendText(exchange, 405, "Method Not Allowed");
    }
  }

  private void handleTaskByIndex(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath(); // expected /api/tasks/{index} or /api/tasks/{index}/complete
    String[] parts = path.split("/");
    if (parts.length < 4) {
      sendText(exchange, 404, "Not Found");
      return;
    }
    String idxStr = parts[3];
    int index;
    try {
      index = Integer.parseInt(idxStr);
    } catch (NumberFormatException e) {
      sendText(exchange, 400, "Invalid index");
      return;
    }
    String method = exchange.getRequestMethod().toUpperCase();
    boolean isCompletePath = parts.length >= 5 && "complete".equalsIgnoreCase(parts[4]);
    try {
      if ("DELETE".equals(method) && parts.length == 4) {
        boolean ok = taskManager.deleteTask(index);
        if (ok) {
          sendJson(exchange, 200, "{\"status\":\"deleted\"}");
        } else {
          sendJson(exchange, 400, "{\"error\":\"Delete failed\"}");
        }
      } else if ("PUT".equals(method) && isCompletePath) {
        boolean ok = taskManager.completeTask(index);
        if (ok) {
          sendJson(exchange, 200, "{\"status\":\"completed\"}");
        } else {
          sendJson(exchange, 400, "{\"error\":\"Complete failed\"}");
        }
      } else {
        sendText(exchange, 405, "Method Not Allowed");
      }
    } catch (Exception e) {
      sendJson(exchange, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
    }
  }

  private List<Task> getTasksSnapshot() {
    try {
      java.lang.reflect.Field f = TaskManager.class.getDeclaredField("tasks");
      f.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<Task> list = (List<Task>) f.get(taskManager);
      return List.copyOf(list);
    } catch (Exception e) {
      return List.of();
    }
  }

  private String toJson(List<Task> tasks) {
    String items = tasks
        .stream().map(t -> "{" + "\"title\":\"" + escape(t.getTitle()) + "\"," + "\"category\":\""
            + escape(t.getCategory()) + "\"," + "\"completed\":" + (t.isCompleted() ? "true" : "false") + "}")
        .collect(Collectors.joining(","));
    return "[" + items + "]";
  }

  private String parseTitle(String body) {
    try {
      int i = body.indexOf("\"title\"");
      if (i < 0)
        return null;
      int c = body.indexOf(':', i);
      if (c < 0)
        return null;
      int q1 = body.indexOf('"', c + 1);
      if (q1 < 0)
        return null;
      int q2 = body.indexOf('"', q1 + 1);
      if (q2 < 0)
        return null;
      return body.substring(q1 + 1, q2);
    } catch (Exception e) {
      return null;
    }
  }

  private void sendHtml(HttpExchange ex, int code, String html) throws IOException {
    byte[] b = html.getBytes(StandardCharsets.UTF_8);
    ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
    ex.sendResponseHeaders(code, b.length);
    try (OutputStream os = ex.getResponseBody()) {
      os.write(b);
    }
  }

  private void sendJson(HttpExchange ex, int code, String json) throws IOException {
    byte[] b = json.getBytes(StandardCharsets.UTF_8);
    ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
    ex.sendResponseHeaders(code, b.length);
    try (OutputStream os = ex.getResponseBody()) {
      os.write(b);
    }
  }

  private void sendText(HttpExchange ex, int code, String text) throws IOException {
    byte[] b = text.getBytes(StandardCharsets.UTF_8);
    ex.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
    ex.sendResponseHeaders(code, b.length);
    try (OutputStream os = ex.getResponseBody()) {
      os.write(b);
    }
  }

  private String escape(String s) {
    return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
  }
}