package coursework3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static coursework3.Constants.SKILL_INDEX_MAP;
import static coursework3.Constants.INDEX_SKILL_MAP;

class Utility {
    /*
     * Get the index of skill
     * @skillName: name of the skill
     */
    public static int getSkillIndex(char skillName) throws IllegalArgumentException {
        Integer index = SKILL_INDEX_MAP.get(skillName);
        if (index == null) {
            throw new IllegalArgumentException("Illegal skill " + skillName);
        }
        return index;
    }

    /*
     * Get the skill corresponding to the index
     * @index: the index
     */
    public static char getSkillName(int index) throws IllegalArgumentException {
        if (INDEX_SKILL_MAP.length <= index) {
            throw new IllegalArgumentException("Cannot find the " + index + "-th skill");
        }
        return INDEX_SKILL_MAP[index];
    }

    /*
     * Serialize a @{Serializable} to a file
     * @s: the object
     * @path: the file to write to
     */
    public static void serializeToFile(Serializable s, String path) throws IOException {
        FileWriter writer = new FileWriter(new File(path), false); // Overwrite any existing file
        writer.write(s.serialize());
        writer.flush();
        writer.close();
    }

    /*
     * Deserialize a @{Serializable} from a file
     * @s: the object
     * @path: the file to read from
     */
    public static void deserializeFromFile(Serializable s, String path) throws IOException, Serializable.DeserializationException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String str = "";
        String line;

        // Read line by line and feed to str
        while ((line = reader.readLine()) != null) {
            str += "\n" + line;
        }

        // Remove the preceeding "\n"
        str = str.substring(1, str.length());

        // Call deserialization
        s.deserialize(str);

        // Release the resource
        reader.close();
    }
}