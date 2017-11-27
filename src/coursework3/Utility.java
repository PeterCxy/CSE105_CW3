package coursework3;

import static coursework3.Constants.SKILL_INDEX_MAP;
import static coursework3.Constants.INDEX_SKILL_MAP;

class Utility {
    /*
     * Get the index of skill
     * @skillName: name of the skill
     */
    public static int getSkillIndex(char skillName) throws IllegalArgumentException {
        Integer index = SKILL_INDEX_MAP.get(skillName);
        if (index == null) {
            throw new IllegalArgumentException("Illegal skill " + skillName);
        }
        return index;
    }

    /*
     * Get the skill corresponding to the index
     * @index: the index
     */
    public static char getSkillName(int index) throws IllegalArgumentException {
        if (INDEX_SKILL_MAP.length <= index) {
            throw new IllegalArgumentException("Cannot find the " + index + "-th skill");
        }
        return INDEX_SKILL_MAP[index];
    }
}