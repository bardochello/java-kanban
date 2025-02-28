package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/prioritized".equals(path)) {
            List<Task> prioritized = taskManager.getPrioritizedTasks();
            sendText(exchange, gson.toJson(prioritized), 200);
        } else {
            sendNotFound(exchange);
        }
    }
}