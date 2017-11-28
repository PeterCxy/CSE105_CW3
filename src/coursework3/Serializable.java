package coursework3;

/*
 * A serializable object is one that can be serialized
 * into a string and vice-versa.
 * 
 * NOTE: Each implementation of Serializable
 *  need to have a zero-argument constructor.
 *  This constraint is not currently allowed by Java.
 */
interface Serializable {
    /*
     * Serialize this object to a String
     * For persistency
     */
    String serialize();

    /*
     * Deserialize this object from a String
     * For persistency
     */
    void deserialize(String str) throws DeserializationException;

    public class DeserializationException extends Exception {
        // Needed by Java's exception system.
        // The number means nothing but just an ID.
        public static final long serialVersionUID = 320731900l;

        public DeserializationException(Throwable e) {
            super(e);
        }

        public DeserializationException(String why) {
            super(why);
        }
    }
}