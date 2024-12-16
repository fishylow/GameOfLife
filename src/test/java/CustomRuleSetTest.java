import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomRuleSetTest {
    private CustomRuleSet ruleSet;

    @Test
    void testBasicSurvivalRules() {
        ruleSet = new CustomRuleSet(new int[]{2, 3}, new int[]{3});

        // Test survival rules
        assertTrue(ruleSet.apply(true, 2), "Cell should survive with 2 neighbors");
        assertTrue(ruleSet.apply(true, 3), "Cell should survive with 3 neighbors");
        assertFalse(ruleSet.apply(true, 1), "Cell should die with 1 neighbor");
        assertFalse(ruleSet.apply(true, 4), "Cell should die with 4 neighbors");
    }

    @Test
    void testBasicBirthRules() {
        ruleSet = new CustomRuleSet(new int[]{2, 3}, new int[]{3});

        // Test birth rules
        assertTrue(ruleSet.apply(false, 3), "Cell should be born with 3 neighbors");
        assertFalse(ruleSet.apply(false, 2), "Cell should not be born with 2 neighbors");
        assertFalse(ruleSet.apply(false, 4), "Cell should not be born with 4 neighbors");
    }

    @Test
    void testEmptyRules() {
        ruleSet = new CustomRuleSet(new int[]{}, new int[]{});

        // Test that nothing survives or is born with empty rules
        assertFalse(ruleSet.apply(true, 2), "No cell should survive with empty rules");
        assertFalse(ruleSet.apply(false, 3), "No cell should be born with empty rules");
    }
}