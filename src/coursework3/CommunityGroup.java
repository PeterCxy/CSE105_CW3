//DO NOT CHANGE THIS PACKAGE
package coursework3;

import cw3interfaces.CommunityGroupInterface;

import java.awt.image.IndexColorModel;
import java.util.ArrayList;

import static coursework3.Constants.SKILL_NUM;

//DO NOT CHANGE THIS NAME
public class CommunityGroup implements CommunityGroupInterface {
    private ArrayList<Volunteer> mVolunteers = new ArrayList<Volunteer>();

    /*
     * Maintain a list of the total number of skills available in this group
     * To avoid repeated calculations
     * The index of this array corresponds to @{Volunteer.mSkillSet}
     */
    private int[] mSkillSet = new int[]{0, 0, 0, 0, 0};
    
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
