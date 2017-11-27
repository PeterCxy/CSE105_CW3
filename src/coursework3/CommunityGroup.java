//DO NOT CHANGE THIS PACKAGE
package coursework3;

import cw3interfaces.CommunityGroupInterface;

import java.util.ArrayList;

//DO NOT CHANGE THIS NAME
public class CommunityGroup implements CommunityGroupInterface {
    private ArrayList<Volunteer> mVolunteers = new ArrayList<Volunteer>();
    
 //COMPLETE THIS CLASS    
    public void addVolunteer(Volunteer vl) {
        mVolunteers.add(vl);
    }  
    
 //these public methods need to form the interface 
// DO NOT CHANGE ANY OF THESE METHOD NAMES, RETURN VALUES, OR ARGUMENTS   
    @Override
    public int howManyVolunteers(){
        //return the total number of volunteers in this community group
        //COMPLETE CODE HERE
        return mVolunteers.size();
    }
    
    @Override
    public String getSkillsTotals(){
        // return the total number of each skill in a String, example:
        //Skill A: 13, Skill B: 20, Skill C: 23, Skill D: 5, Skill E: 41
        //COMPLETE CODE HERE
        return "";
    }
            
    
    
    
    

}
