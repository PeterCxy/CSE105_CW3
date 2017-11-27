
package coursework3;

/*READ ME: Use this class as your main class, and create your menu here
Your menu should then call the appropriate methods in the SkillSorter class
You need to complete the other classes, including the empty methods.
/*
*/
public class CW3Main {
    
    public static void main(String[] args){
        CommunityGroup group = new CommunityGroup();
        group.addVolunteer(new Volunteer("ABC"));
        group.addVolunteer(new Volunteer("BEE"));
        System.out.println(group.getSkillsTotals());
        group.addVolunteer(new Volunteer("CAA"));
        System.out.println(group.getSkillsTotals());
        group.removeVolunteer("CAA");
        System.out.println(group.getSkillsTotals());
        System.out.println(group.removeVolunteer("CEA"));
        System.out.println(group.getSkillsTotals());
    }
    
    //Construct and run your menu here.
    //You MUST call methods in SkillSorter from your menu
    //and complete the methods in SkillSorter 
    //DO NOT write the methods, eg addVolunteer, in THIS class.
    //Call and use the ones in SkillSorter.
}
