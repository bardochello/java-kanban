import controllers.HistoryManager;
import controllers.Manager;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    @Test
    void createTaskManagerDefault() {
        TaskManager taskManager = Manager.getDefault();
        assertNotNull(taskManager, "TaskManager should be created");
    }

    @Test
    void createHistoryManagerDefault() {
        HistoryManager historyManager = Manager.getDefaultHistory();
        assertNotNull(historyManager, "HistoryManager should be created");
    }
}