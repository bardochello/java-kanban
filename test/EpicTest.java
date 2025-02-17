import tasks.Epic;
import org.junit.jupiter.api.Test;
import tasks.TaskType;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void EpicsWithSameIdShouldBeEquals() {
        int epicId = 1;
        Epic epic1 = new Epic("epic1", "epic1 description", TaskType.EPIC);
        epic1.setId(epicId);
        Epic epic2 = new Epic("epic2", "epic2 description", TaskType.EPIC);
        epic2.setId(epicId);
        assertEquals(epic1, epic2, "Epics are not equals");
    }
}