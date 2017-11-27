package coursework3;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    // Total number of possible skills
    public static final int SKILL_NUM = 5;

    // Map from skill name to its index
    public static final Map<Character, Integer> SKILL_INDEX_MAP = new HashMap<>();

    // It should have been reverse of SKILL_INDEX_MAP
    // But since it's Map<Integer, Character>, we can simply use an array.
    public static final char[] INDEX_SKILL_MAP = new char[]{'A', 'B', 'C', 'D', 'E'};

    static {
        // Build SKILL_INDEX_MAP from INDEX_SKILL_MAP
        for (int i = 0; i < INDEX_SKILL_MAP.length; i++) {
            SKILL_INDEX_MAP.put(INDEX_SKILL_MAP[i], i);
        }
    }
}