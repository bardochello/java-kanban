package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import http.HttpMethod;
import tasks.Epic;
import tasks.NotFoundException;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (HttpMethod.GET.equals(method) && "/epics".equals(path)) {
                List<Epic> epics = taskManager.getEpics();
                sendText(exchange, gson.toJson(epics), 200);
            } else if (HttpMethod.POST.equals(method) && "/epics".equals(path)) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                Epic epic = gson.fromJson(requestBody, Epic.class);
                if (epic.getId() == 0) {
                    taskManager.addEpic(epic);
                    sendText(exchange, "{\"message\":\"Эпик создан\"}", 201);
                } else {
                    taskManager.updateEpic(epic);
                    sendText(exchange, "{\"message\":\"Эпик обновлен\"}", 200);
                }
            } else if (HttpMethod.DELETE.equals(method) && path.startsWith("/epics/")) {
                int id = Integer.parseInt(path.substring("/epics/".length()));
                taskManager.deleteEpicByID(id);
                sendText(exchange, "{\"message\":\"Эпик удален\"}", 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendText(exchange, "{\"error\":\"Неверный формат JSON\"}", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }
}