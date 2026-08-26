package org.finetree.fineharvest.quests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestsIntegrationTest {

    @Test
    void supportsLMBishopQuests() {
        assertTrue(QuestsIntegration.supportsMainClass(
                "com.leonardobishop.quests.bukkit.BukkitQuestsPlugin"));
    }

    @Test
    void supportsPikaMugQuests() {
        assertTrue(QuestsIntegration.supportsMainClass("me.pikamug.quests.Quests"));
    }

    @Test
    void rejectsUnrelatedPluginsNamedQuests() {
        assertFalse(QuestsIntegration.supportsMainClass("example.quests.Quests"));
    }
}
