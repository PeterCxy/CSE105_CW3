package coursework3;

import cw3interfaces.VolunteerInterface;

import static coursework3.Constants.SKILL_NUM;

public class Volunteer implements VolunteerInterface {
    /*
     * Each entry of this array represents
     * the number of 'instances' a person have for one specific skill
     * See @{Constants.SKILL_INDEX_MAP} for how this array is arranged
     */
    private int[] mSkillSet = new int[]{0, 0, 0, 0, 0};

    /*
     * Constructor of Volunteer class
     * @skills: a string that represents the skills a person has
     *   Must have a length of 3. Must contain only A, B, C, D or E
     *   e.g. "AAB" "CCD" "ABE"
     */
    public Volunteer(String skills) {
        parseSkillStr(skills);
    }

    /*
     * Get the number of a skill based on its name
     */
    public int getSkillPoint(char skillName) {
        return getSkillPoint(Utility.getSkillIndex(skillName));
    }

    /*
     * Get the number of a skill based on its index
     */
    public int getSkillPoint(int index) {
        return mSkillSet[index];
    }
    
    /*
     * Returns the string representation of the skill set
     */
    @Override
    public String getSkillSet() {
        StringBuilder setBuilder = new StringBuilder();
        for (int i = 0; i < SKILL_NUM; i++) {
            if (mSkillSet[i] > 0) {
                // Append the corresponding char representation of the skill
                // for each occurance
                for (int j = 0; j < mSkillSet[i]; j++)
                    setBuilder.append(Utility.getSkillName(i));
            }
        }

        return setBuilder.toString();
    }

    /*
     * Overrides @{java.lang.Object.equals()} for use in ArrayList (@{CommunityGroup})
     * Compare the equality of two volunteers based on the skill set
     * Note: the skill set is the only thing that matters in this program
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Volunteer.class) {
            return false;
        } else {
            return this.getSkillSet().equals(((Volunteer) obj).getSkillSet());
        }
    }

    /*
     * Parse the skills string into the mSkillSet array
     * See @{mSkillSet} for details.
     */
    private void parseSkillStr(String str) {
        if (str.length() != 3) {
            throw new IllegalArgumentException("A person can only have 3 skills");
        }

        for (int i = 0; i < 3; i++) {
            mSkillSet[Utility.getSkillIndex(str.charAt(i))]++; // Increase the occurence by once
        }
    }
    
}
