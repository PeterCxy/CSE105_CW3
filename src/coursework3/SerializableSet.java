package coursework3;

import java.util.List;
import java.util.regex.Pattern;

/*
 * A set (list) of serializable objects that is also serializable.
 * Each of these sets should specify its own unique separator
 * The separator could have been generated automatically given that it is long enough
 * But for this program, since it is small and manageble, we can just manually give them
 * different separators.
 * If the project could grow large, this base class can be easily extended
 * to support auto-generated unique separators.
 */
abstract class SerializableSet<T extends Serializable> implements Serializable {
    /*
     * It is not allowed in Java to create instance of generic types directly
     * e.g. new T() is invalid in Java
     * Thus, we will need to store a Class object of that Class
     * This should be passed over by the constructor
     */
    private Class<T> mClazz = null;

    /*
     * The list of serializable objects
     */
    private List<T> mList = null;

    /*
     * The seprator
     * should be given in the constructor
     */
    private String mSeparator = null;

    /*
     * This constructor should only be used from the subclasses
     * to give parameters needed by this base class.
     * Trying to give the most freedom to subclasses.
     * Note: @{setList()} must be called right after construction.
     *   The list could not be given as a constructor parameter,
     *   because Java disallows the use of instance fields while
     *   calling super(), while in many cases the List will be 
     *   an instance field.
     * 
     * @clazz: the Class object of the type of `T`.
     * @separator: the separator used to serialize / deserialize.
     */
    protected SerializableSet(Class<T> clazz, String separator) {
        mClazz = clazz;
        mSeparator = separator;
    }
    
    /*
     * If the list object is recreated, call this method.
     * @list: the new list.
     */
    protected void setList(List<T> list) {
        mList = list;
    }

    /*
     * Clear the current List object.
     * Override this in case you need to hook into
     * the clearing step before deserialization.
     */
    protected void clear() {
        mList.clear();
    }

    /*
     * Add an item to the list.
     * Override this if you need special processing
     * while deserializing every item.
     */
    protected void add(T item) {
        mList.add(item);
    }

    /*
     * Serialize this object to a String
     * For persistency
     */
    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();

        // Append all the serialized items to the string
        // separated with the separator.
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mSeparator);
            sb.append(mList.get(i).serialize());
        }

        // Remove the first unneeded separator
        return sb.substring(mSeparator.length(), sb.length());
    }
    
    /*
     * Deserialize this object from a String
     * For persistency
     */
    @Override
    public void deserialize(String str) throws Serializable.DeserializationException {
        clear(); // Clear the list first.

        // The special characters in mSeparator should be escaped
        // because @{java.lang.String.split()} interprets the argument
        // as a regular expression.
        String[] items = str.split(Pattern.quote(mSeparator));

        // Recreate each item from the serialized form
        for (int i = 0; i < items.length; i++) {
            T item = null;

            try {
                item = mClazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // Throw it as a fatal Error
                throw new RuntimeException("A Serializable must have a public zero-argument constructor.");
            }

            item.deserialize(items[i]);
            add(item);
        }
    }
}