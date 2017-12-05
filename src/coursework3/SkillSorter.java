package coursework3;

import cw3interfaces.SkillSorterInterface;

import java.util.ArrayList;
import java.util.HashMap;

import static coursework3.Constants.SKILL_NUM;

public class SkillSorter extends SerializableSet<CommunityGroup> implements SkillSorterInterface {
    private static final String SEPARATOR = "|";
    private ArrayList<CommunityGroup> myGroups = new ArrayList<>();
    
    /*
     * Initialization and stuff needed by @{SerializableSet<T>}
     */
    public SkillSorter() {
        super(CommunityGroup.class, SEPARATOR, false);
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
     * This returns the group that the volunteer is added to.
     * Since the defined @{cw3interfaces.SkillSorterInterface} has already
     * limited the return type of this function, we need a different name here.
     * 
     * @vol: the volunteer
     */
    public int myAddVolunteer(Volunteer vol) {
        int best = bestGroup(vol);

        if (best < 0) {
            throw new IllegalStateException("Could not find the best group. All the groups may be full. Aborting.");
        }

        myGroups.get(best).addVolunteer(vol);
        return best;
    }

    /*
     * Same as @{myAddVolunteer}, to conform with
     * the @{cw3interfaces.SkillSorterInterface} definition
     */
    @Override
    public void addVolunteer(Volunteer vol) {
        myAddVolunteer(vol);
    }
    
    /*
     * Delete a volunteer from a group and add to another group
     * If no such volunteer is found, @{java.lang.IllegalArgumentException} will be thrown
     * If the target group is full, @{java.lang.IllegalStateException} will be thrown
     * 
     * @skillSet: the skills the volunteer has. if invalid,
     *  @{java.lang.IllegalArgumentException} will be thrown.
     * @from: the original group
     * @to: the new group
     */
    @Override
    public void moveVolunteer(String skillSet, CommunityGroup from, CommunityGroup to) throws IllegalArgumentException, IllegalStateException {
        // We need to add first
        // because if the target group is full, the whole operation can break here.
        to.addVolunteer(new Volunteer(skillSet));
        deleteVolunteer(skillSet, from);
    }

    /*
     * Same but with group indexes instead of group objects
     */
    public void moveVolunteer(String skillSet, int fromIndex, int toIndex) throws IllegalArgumentException, IllegalStateException {
        assertGroup(fromIndex);
        assertGroup(toIndex);
        moveVolunteer(skillSet, myGroups.get(fromIndex), myGroups.get(toIndex));
    }
    
    /*
     * Delete a volunteer with some skills from a group
     * If no such volunteer is found, @{java.lang.IllegalArgumentException} will be thrown
     * 
     * @skillSet: the skills the volunteer has. if invalid,
     *  @{java.lang.IllegalArgumentException} will be thrown.
     * @from: the group to delete from
     */
    @Override
    public void deleteVolunteer(String skillSet, CommunityGroup from) throws IllegalArgumentException {
        if (!from.removeVolunteer(skillSet)) {
            throw new IllegalArgumentException("Skill set " + skillSet + " not found.");
        }
    }

    /*
     * Same but with group indexes instead of group objects
     */
    public void deleteVolunteer(String skillSet, int fromIndex) throws IllegalArgumentException {
        assertGroup(fromIndex);
        deleteVolunteer(skillSet, myGroups.get(fromIndex));
    }
    
    /*
     * Remove all volunteers from all groups
     */
    @Override
    public void deleteAllVolunteers() {
        for (int i = 0; i < myGroups.size(); i++) {
            myGroups.get(i).clearVolunteers();
        }
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
            System.out.println(i + ": " + myGroups.get(i).toString());
        }
    }

    /*
     * Retrieve status on how many volunteers
     * the specified group has for each possible combination of skills
     * @groupIndex: the group.
     */
    public HashMap<String, Integer> getStats(int groupIndex) {
        assertGroup(groupIndex);
        return myGroups.get(groupIndex).getStats();
    }

    /*
     * Return nothing if the index points to a valid group
     * otherwise throw a @{java.lang.IllegalArgumentException}
     */
    private void assertGroup(int index) throws IllegalArgumentException {
        if (index >= myGroups.size()) {
            throw new IllegalArgumentException("Illegal group index " + index);
        }
    }

    /*
     * Return an array of the points of skill X across all groups
     */
    private int[] getSkillPoints(int skillIndex) {
        int[] ret = new int[myGroups.size()]; // The array is fixed-length
        for (int i = 0; i < myGroups.size(); i++) {
            ret[i] = myGroups.get(i).getSkillPoint(skillIndex);
        }
        return ret;
    }

    /*
     * Return an array of the total members in each group
     */
    private int[] getMemberTotals() {
        int[] ret = new int[myGroups.size()];
        for (int i = 0; i < myGroups.size(); i++) {
            ret[i] = myGroups.get(i).howManyVolunteers();
        }
        return ret;
    }

    /*
     * Determine which group to add a volunteer to
     * that will make the cost function (defined below) be closest to 0
     * return -1 if no such group could be found (e.g. all of them are full)
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
     *   (V(A) + V(B) + V(C) + V(D) + V(E) + V(S)) / 6
     *  where
     *   V(X) is the variance of skill X across all groups
     *   V(S) is the variance of the sizes of all groups
     * Exceptions: when a group is full, trying to add members to it
     *   will always result in a cost of Double.MAX_VALUE
     * 
     * @vl: the volunteer
     * @groupIndex: which group to add to
     */
    private double costFunc(Volunteer vl, int groupIndex) {
        if (myGroups.get(groupIndex).isFull()) {
            // If a group is full, set its cost to MAX_VALUE
            // to avoid adding more members into it
            // If every group is full, the program will
            // throw an exception anyway.
            return Double.MAX_VALUE;
        }

        double cost = 0f;

        // Add up all the cost caused by the distribution of skills
        for (int i = 0; i < SKILL_NUM; i++) {
            cost += varianceSkill(i, vl, groupIndex);
        }

        // and the cost caused by the distributon of sizes
        cost += varianceSize(groupIndex);

        return cost / (myGroups.size() + 1);
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
        // Get the current skill points
        int[] skillPoints = getSkillPoints(skillIndex);

        // Pretend to add the volunteer to one group
        skillPoints[groupIndex] += vl.getSkillPoint(skillIndex);

        // Calculate the variance
        return Utility.variance(skillPoints);
    }

    /*
     * Calculate the variance of the size across the groups
     * after adding a volunteer to a group
     * This will not actually add the volunteer
     * @groupIndex: the group to add to
     */
    private double varianceSize(int groupIndex) {
        int[] totals = getMemberTotals();
        totals[groupIndex] += 1;
        return Utility.variance(totals);
    }
}
