package coursework3;

import cw3interfaces.SkillSorterInterface;

import java.util.ArrayList;

import static coursework3.Constants.SKILL_NUM;

public class SkillSorter extends SerializableSet<CommunityGroup> implements SkillSorterInterface {
    private static final String SEPARATOR = "|";
    private ArrayList<CommunityGroup> myGroups = new ArrayList<>();
    
    /*
     * Initialization and stuff needed by @{SerializableSet<T>}
     */
    public SkillSorter() {
        super(CommunityGroup.class, SEPARATOR);
        setList(myGroups);
        for (int i = 0; i < 5; i++) {
            myGroups.add(new CommunityGroup());
        }
    }

    /*
     * Override the deserialize method to check the group number
     * make sure we always have 5 groups
     */
    @Override
    public void deserialize(String str) throws Serializable.DeserializationException {
        super.deserialize(str);
        if (myGroups.size() != 5) {
            throw new Serializable.DeserializationException("Group number isn't 5");
        }
    }
    
    /*
     * Add the volunteer to some group such that
     * the distribution of the skills and the total members of each group
     * are best balanced.
     * 
     * @vol: the volunteer
     */
    @Override
    public void addVolunteer(Volunteer vol) {
        myGroups.get(bestGroup(vol)).addVolunteer(vol);
    }
    
    @Override
    public void moveVolunteer(String skillSet, CommunityGroup from, CommunityGroup to) {
        //move a volunteer with this skillset (eg AAA, BCD) from one CommunityGroup to another
        //COMPLETE CODE HERE
    }
    
    @Override
    public void deleteVolunteer(String skillSet, CommunityGroup from) {
        //delete a volunteer with this skillset from this CommunityGroup
        //COMPLETE CODE HERE
    }
    
    @Override
    public void deleteAllVolunteers() {
        // delete all volunteers from all CommunityGroups
        //COMPLETE CODE HERE
    }

    @Override
    public ArrayList<CommunityGroup> getCommunityGroups() {
        //return an ArrayList of all this application's CommunityGroups
        return myGroups;
    }

    /*
     * Print all the statistics about all the groups
     * One line for one group
     */
    public void printAll() {
        for (int i = 0; i < myGroups.size(); i++) {
            System.out.println(myGroups.get(i).toString());
        }
    }

    /*
     * Determine which group to add a volunteer to
     * that will make the cost function (defined below) be closest to 0
     * @vl: the volunteer
     */
    private int bestGroup(Volunteer vl) {
        double best = Double.MAX_VALUE;
        int bestGroup = -1;
        for (int i = 0; i < myGroups.size(); i++) {
            double cost = costFunc(vl, i);
            if (cost < best) { // Find the minimum possible value of the cost function
                best = cost;
                bestGroup = i;
            }
        }
        return bestGroup;
    }

    /*
     * Define a cost function to determine how well the groups are balanced
     * after adding a volunteer to a group
     * The closer the value is to 0, the better the distribution will be.
     * This will not actually add the volunteer
     * Formula:
     *   (V(A) + V(B) + V(C) + V(D) + V(E) + V(S)) / N
     *  where
     *   V(X) is the variance of skill X across all groups
     *   V(S) is the variance of the sizes of all groups
     *   N is the number of groups
     * 
     * @vl: the volunteer
     * @groupIndex: which group to add to
     */
    private double costFunc(Volunteer vl, int groupIndex) {
        double cost = 0f;

        // Add up all the cost caused by the distribution of skills
        for (int i = 0; i < SKILL_NUM; i++) {
            cost += varianceSkill(i, vl, groupIndex);
        }

        // and the cost caused by the distributon of sizes
        cost += varianceSize(groupIndex);

        return cost / myGroups.size();
    }

    /*
     * Calculate the variance of a skill across all groups
     * after adding a volunteer to a group
     * This will not actually add the volunteer
     * @skillIndex: which skill to calculate
     * @vl: the new volunteer
     * @groupIndex: the group to add to
     */
    private double varianceSkill(int skillIndex, Volunteer vl, int groupIndex) {
        // Calculate the average which is needed by the variance
        double average = vl.getSkillPoint(skillIndex); // Start with our new guy
        for (int i = 0; i < myGroups.size(); i++) {
            average += myGroups.get(i).getSkillPoint(skillIndex);
        }
        average = average / myGroups.size();

        // Actually calculate the variance
        double variance = 0f;
        for (int i = 0; i < myGroups.size(); i++) {
            double diff = myGroups.get(i).getSkillPoint(skillIndex) - average;
            if (i == groupIndex) {
                // The new person is here
                diff += vl.getSkillPoint(skillIndex);
            }
            variance += diff * diff;
        }

        return variance / myGroups.size();
    }

    /*
     * Calculate the variance of the size across the groups
     * after adding a volunteer to a group
     * This will not actually add the volunteer
     * @groupIndex: the group to add to
     */
    private double varianceSize(int groupIndex) {
        double average = 1f;
        for (int i = 0; i < myGroups.size(); i++) {
            average += myGroups.get(i).howManyVolunteers();
        }
        average = average / myGroups.size();

        double variance = 0f;
        for (int i = 0; i < myGroups.size(); i++) {
            double diff = myGroups.get(i).howManyVolunteers() - average;
            if (i == groupIndex) {
                diff += 1;
            }
            variance += diff * diff;
        }

        return variance / myGroups.size();
    }
}
