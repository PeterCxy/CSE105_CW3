package coursework3;

import cw3interfaces.CommunityGroupInterface;

import java.util.ArrayList;

import static coursework3.Constants.SKILL_NUM;

public class CommunityGroup extends SerializableSet<Volunteer> implements CommunityGroupInterface {
    private static final String SEPARATOR = ",";
    private ArrayList<Volunteer> mVolunteers = new ArrayList<Volunteer>();

    /*
     * Maintain a list of the total number of skills available in this group
     * To avoid repeated calculations
     * The index of this array corresponds to @{Volunteer.mSkillSet}
     */
    private int[] mSkillSet = new int[]{0, 0, 0, 0, 0};

    /*
     * Constructor to explicitly handle things needed by @{SerializableSet<T>}
     */
    public CommunityGroup() {
        super(Volunteer.class, SEPARATOR);
        setList(mVolunteers);
    }
    
    /*
     * Add a volunteer to this group
     * This does nothing to balance the groups
     */
    public void addVolunteer(Volunteer vl) {
        mVolunteers.add(vl);

        // Add the skills of this single volunteer to the totals
        for (int i = 0; i < SKILL_NUM; i++) {
            mSkillSet[i] += vl.getSkillPoint(i);
        }
    }

    /*
     * Remove a volunteer from this group
     * Note that we don't distinguish volunteers with the same skillsets
     * @return: true if the volunteer is found and deleted
     */
    public boolean removeVolunteer(Volunteer vl) {
        if (!mVolunteers.contains(vl)) {
            return false;
        }
        mVolunteers.remove(vl);

        // Remove the skills of this single volunteer from the totals
        for (int i = 0; i < SKILL_NUM; i++) {
            mSkillSet[i] -= vl.getSkillPoint(i);
        }
        return true;
    }

    /*
     * Remove a volunteer with the specified skill set from this group
     * @skills: the string of the skill set of this volunteer
     * @return: true if the volunteer is found and deleted
     */
    public boolean removeVolunteer(String skills) throws IllegalArgumentException {
        return removeVolunteer(new Volunteer(skills));
    }

    /*
     * Clear everything in this group
     */
    public void clearVolunteers() {
        mVolunteers.clear();
        mSkillSet = new int[]{0, 0, 0, 0, 0};
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
     * Alias of @{addVolunteer()} but overrides the super class method
     * To ensure the totals are calculated correctly on deserialization
     * (this method will be called while deserialization when adding items)
     */
    @Override
    protected void add(Volunteer vl) {
        addVolunteer(vl);
    }
    
    /*
     * Returns the total number of volunteers available from this group
     */
    @Override
    public int howManyVolunteers() {
        return mVolunteers.size();
    }
    
    /*
     * Returns the total number of each available skill in this group
     * e.g. Skill A: 13, Skill B: 20, Skill C: 23, Skill D: 5, Skill E: 41
     */
    @Override
    public String getSkillsTotals() {
        StringBuilder totalBuilder = new StringBuilder();
        for (int i = 0; i < SKILL_NUM; i++) {
            totalBuilder.append(", ");
            totalBuilder.append("Skill ");
            totalBuilder.append(Utility.getSkillName(i));
            totalBuilder.append(": ");
            totalBuilder.append(mSkillSet[i]);
        }
        return totalBuilder.substring(2, totalBuilder.length()).toString();
    }

    /*
     * Overriding @{java.lang.Object} method
     * Alias of @{getSkillsTotals}
     */
    @Override
    public String toString() {
        return getSkillsTotals();
    }
}
