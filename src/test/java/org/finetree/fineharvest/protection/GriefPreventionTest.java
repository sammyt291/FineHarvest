package org.finetree.fineharvest.protection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GriefPreventionTest {

    @Test
    void allowsBuildingOutsideClaims() {
        assertTrue(GriefPrevention.canBuild(null, null));
    }
}
