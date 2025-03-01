package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import http.HttpMethod;
import tasks.NotFoundException;
import tasks.SubTask;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.GET.equals(method) && "/subtasks".equals(path)) {
                List<SubTask> subtasks = taskManager.getSubtasks();
                sendText(exchange, gson.toJson(subtasks), 200);
            } else if (HttpMethod.POST.equals(method) && "/subtasks".equals(path)) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                SubTask subtask = gson.fromJson(requestBody, SubTask.class);
                if (subtask.getId() == 0) {
                    taskManager.addTask(subtask);
                    sendText(exchange, "{\"message\":\"Подзадача создана\"}", 201);
                } else {
                    taskManager.updateSubtask(subtask);
                    sendText(exchange, "{\"message\":\"Подзадача обновлена\"}", 200);
                }
            } else if (HttpMethod.DELETE.equals(method) && path.startsWith("/subtasks/")) {
                int id = Integer.parseInt(path.substring("/subtasks/".length()));
                taskManager.deleteSubtaskByID(id);
                sendText(exchange, "{\"message\":\"Подзадача удалена\"}", 200);
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
            sendInternalError(exchange);
        }
    }
}