package nl.tue.vrp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * IDManager will manage the uniqueness of the id.
 * It'll also store the tied Object for searching.
 */
public class IDManager {
    private static IDManager INSTANCE;

    private int startPoint;
    private final Map<Integer, Object> usedMap;

    private IDManager() {
        this.startPoint = 1;
        usedMap = new HashMap<>();
    }

    public static IDManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IDManager();
        }
        return INSTANCE;
    }

    /**
     * @return return new id after being registered.
     */
    public int getNewID() {
        while (usedMap.get(startPoint) != null) {
            startPoint++;
        }
        usedMap.put(startPoint, true);
        return startPoint++;
    }

    /**
     * @param obj the object to be registered
     * @return return new id for the object and register the id with the object.
     */
    public int getNewID(Object obj) {
        while (usedMap.get(startPoint) != null) {
            startPoint++;
        }
        usedMap.put(startPoint, true);
        return startPoint++;
    }

    public int getNewIDAfter(int offset) {
        if (offset <= startPoint) {
            while (usedMap.get(offset) != null) {
                offset++;
            }
            usedMap.put(offset, true);
            startPoint = offset + 1;
            return offset;
        }
        while (usedMap.get(offset) != null) {
            offset++;
        }
        usedMap.put(offset, true);
        startPoint = offset + 1;
        return offset;
    }

    /**
     * @param id the id in positive integer
     * @return true if the id is successfully registered, false if the id is already registered.
     */
    public boolean registerID(int id) {
        if (usedMap.containsKey(id)) {
            return false;
        }
        usedMap.put(id, true);
        if (id == startPoint) {
            startPoint++;
        }
        return true;
    }

    /**
     * @param id the id of object.
     * @param obj the object to be registered
     * @return true if the object is successfully registered with the specified id.
     * Otherwise, return false if the id is already registered
     */
    public boolean registerID(int id, Object obj) {
//        if (usedMap.containsKey(id)) {
//            return false;
//        }
        usedMap.put(id, obj);
        if (id == startPoint) {
            startPoint++;
        }
        return true;
    }

    /**
     * @param id The id of the object
     * @return return the object of the registered id.
     * If the id is registered without object, then the object will be Boolean true.
     * return null, if the id doesn't exist.
     */
    public Object getObject(int id) {
        return usedMap.get(id);
    }

    /**
     * @param id The id.
     * @param obj The object to be assigned to the id.
     * @return if the id doesn't exist, return false.
     * Otherwise, return true and the existing object for the id is replaced.
     */
    public boolean assignExistingID(int id, Object obj) {
        if (usedMap.containsKey(id)) {
            usedMap.put(id, obj);
            return true;
        }
        return false;
    }

    /**
     * @param id the id to be unregistered.
     * @return true if the id is successfully unregistered. Otherwise, false if the id doesn't exist.
     */
    public boolean unregisterID(int id) {
        if (usedMap.remove(id) != null) {
            if (id < startPoint) {
                startPoint = id;
            }
            return true;
        }
        return false;
    }
}
