//DO NOT CHANGE THIS PACKAGE
package coursework3;

import cw3interfaces.SkillSorterInterface;
import java.util.ArrayList;

//DO NOT CHANGE THIS NAME
public class SkillSorter implements SkillSorterInterface{
    private ArrayList<CommunityGroup> myGroups = new ArrayList<>();
    
    //COMPLETE THIS CLASS
    public SkillSorter() {
        for (int i = 0; i < 5; i++) {
            myGroups.add(new CommunityGroup());
        }
    }
    
//these public methods need to form the interface 
// DO NOT CHANGE ANY OF THESE METHOD NAMES, RETURN VALUES, OR ARGUMENTS
    @Override
    public void addVolunteer(Volunteer vol){
        //add a volunteer to a Community Group USING YOUR SORTING ALGORITHM
        //COMPLETE CODE HERE
    }
    
    @Override
    public void moveVolunteer(String skillSet, CommunityGroup from, CommunityGroup to){
        //move a volunteer with this skillset (eg AAA, BCD) from one CommunityGroup to another
        //COMPLETE CODE HERE
    }
    
    @Override
    public void deleteVolunteer(String skillSet, CommunityGroup from){
        //delete a volunteer with this skillset from this CommunityGroup
        //COMPLETE CODE HERE
    }
    
    @Override
    public void deleteAllVolunteers(){
        // delete all volunteers from all CommunityGroups
        //COMPLETE CODE HERE
    }

    @Override
    public ArrayList<CommunityGroup> getCommunityGroups(){
        //return an ArrayList of all this application's CommunityGroups
        return myGroups;
    }
    
   
   
}
