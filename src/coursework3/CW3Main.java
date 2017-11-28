package coursework3;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;

import static coursework3.Constants.INDEX_SKILL_MAP;

public class CW3Main {
    private static final SkillSorter sSorter = new SkillSorter();
    
    public static void main(String[] args) {
        // TODO: Remove testing code
        Scanner scanner = new Scanner(System.in);
        feedRandomData(scanner.nextInt());
        HashMap<String, Integer> map = sSorter.getStats(0);
        for (String k : map.keySet()) {
            System.out.println(k + "\t" + map.get(k));
        }
        String s = sSorter.serialize();
        System.out.println(s);
        SkillSorter nSorter = new SkillSorter();

        try {
            nSorter.deserialize(s);
            nSorter.printAll();
        } catch (Serializable.DeserializationException e) {
            throw new RuntimeException(e);
        }

        nSorter.deleteAllVolunteers();
        nSorter.printAll();
    }
    
    /*
     * FOR TESTING PURPOSE
     * Feed random data to the sorter and print out the final distribution
     */
    private static void feedRandomData(int num) {
        for (int i = 0; i < num; i++) {
            sSorter.addVolunteer(new Volunteer(randomSkills()));
        }
        sSorter.printAll();
    }

    /*
     * FOR TESTING PURPOSE
     * Generate random skill set
     */
    private static String randomSkills() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(INDEX_SKILL_MAP[new Random().nextInt(INDEX_SKILL_MAP.length)]);
        }
        return sb.toString();
    }
}
