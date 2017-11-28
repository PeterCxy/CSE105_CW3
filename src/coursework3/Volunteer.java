package coursework3;

import cw3interfaces.VolunteerInterface;

import static coursework3.Constants.SKILL_NUM;

public class Volunteer implements VolunteerInterface, Serializable {
    /*
     * Each entry of this array represents
     * the number of 'instances' a person have for one specific skill
     * See @{Constants.SKILL_INDEX_MAP} for how this array is arranged
     */
    private int[] mSkillSet = new int[]{0, 0, 0, 0, 0};

    /*
     * Zero-argument constructor
     * used when deserialization is needed
     * CALL deserialize() RIGHT AFTER USING THIS CONSTRUCTOR
     * OTHERWISE THE PROGRAM WILL GO REALLY WRONG
     */
    public Volunteer() {
        // Nothing to do.
    }

    /*
     * Constructor of Volunteer class
     * @skills: a string that represents the skills a person has
     *   Must have a length of 3. Must contain only A, B, C, D or E
     *   e.g. "AAB" "CCD" "ABE"
     */
    public Volunteer(String skills) throws IllegalArgumentException {
        parseSkillStr(skills);
    }

    /*
     * Get the number of a skill based on its name
     */
    public int getSkillPoint(char skillName) throws IllegalArgumentException {
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
     * Serialize this object to a String
     * For persistency
     * Here it simply equals getSkillSet()
     */
    @Override
    public String serialize() {
        return getSkillSet();
    }

    /*
     * Deserialize this object from a String
     * For persistency
     * This is basically equivalent to the one-parameter constructor
     * but this is needed to implement the Serializable interface.
     */
    @Override
    public void deserialize(String str) throws Serializable.DeserializationException {
        try {
            parseSkillStr(str);
        } catch (IllegalArgumentException e) {
            throw new Serializable.DeserializationException(e);
        }
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
    private void parseSkillStr(String str) throws IllegalArgumentException {
        if (str.length() != 3) {
            throw new IllegalArgumentException("A person can only have 3 skills");
        }

        for (int i = 0; i < 3; i++) {
            mSkillSet[Utility.getSkillIndex(str.charAt(i))]++; // Increase the occurence by once
        }
    }
    
}
