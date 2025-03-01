package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import http.HttpMethod;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (HttpMethod.GET.equals(method) && "/history".equals(path)) {
            List<Task> history = taskManager.getHistory();
            sendText(exchange, gson.toJson(history), 200);
        } else {
            sendNotFound(exchange);
        }
    }
}