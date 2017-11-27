//DO NOT CHANGE THIS PACKAGE
package coursework3;

import cw3interfaces.VolunteerInterface;

import static coursework3.Constants.SKILL_INDEX_MAP;
import static coursework3.Constants.INDEX_SKILL_MAP;

//DO NOT CHANGE THIS NAME
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
    
    // these public methods need to form the interface 
    // DO NOT CHANGE ANY OF THESE METHOD NAMES, RETURN VALUES, OR ARGUMENTS   
    @Override
    public String getSkillSet() {
        //COMPLETE CODE HERE
        //returns a String of this volunteers skills, eg BBB, ABC, CDD etc
        StringBuilder setBuilder = new StringBuilder();
        for (int i = 0; i < INDEX_SKILL_MAP.length; i++) {
            if (mSkillSet[i] > 0) {
                // Append the corresponding char representation of the skill
                // for each occurance
                for (int j = 0; j < mSkillSet[i]; j++)
                    setBuilder.append(INDEX_SKILL_MAP[i]);
            }
        }

        return setBuilder.toString();
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
            // Get the index of corresponding skill in the mSkillSet array
            Integer index = SKILL_INDEX_MAP.get(str.charAt(i));
            if (index == null) { // If null, this means the character is illegal
                throw new IllegalArgumentException("Illegal character " + str.charAt(i));
            } else {
                mSkillSet[index]++; // Increase the occurence by once
            }
        }
    }
    
}
