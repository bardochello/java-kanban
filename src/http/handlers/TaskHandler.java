package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import http.HttpMethod;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.GET.equals(method) && "/tasks".equals(path)) {
                List<Task> tasks = taskManager.getTasks();
                sendText(exchange, gson.toJson(tasks), 200);
            } else if (HttpMethod.POST.equals(method) && "/tasks".equals(path)) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                Task task = gson.fromJson(requestBody, Task.class);
                if (task.getId() == 0) {
                    taskManager.addTask(task);
                    sendText(exchange, "{\"message\":\"Задача создана\"}", 201);
                } else {
                    taskManager.updateTask(task);
                    sendText(exchange, "{\"message\":\"Задача обновлена\"}", 200);
                }
            } else if (HttpMethod.DELETE.equals(method) && path.startsWith("/tasks/")) {
                int id = Integer.parseInt(path.substring("/tasks/".length()));
                taskManager.deleteTaskByID(id);
                sendText(exchange, "{\"message\":\"Задача удалена\"}", 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendText(exchange, "{\"error\":\"Неверный формат JSON\"}", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }
}