import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    @Test
    void createTaskManagerDefault() {
        TaskManager taskManager = Manager.getDefault();
        assertNotNull(taskManager, "TaskManager didn't created");
    }

    @Test
    void createHistoryManagerDefault() {
        HistoryManager historyManager = Manager.getDefaultHistory();
        assertNotNull(historyManager, "HistoryTaskManager didn't created");
    }
}