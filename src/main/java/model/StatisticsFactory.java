package model;

import java.util.HashMap;

/***
 * A factory that creates specific data packages depending on what method is called.
 */
public final class StatisticsFactory {

    /***
     * Creates a hashmap containing the ITags and the number of events tagged on it. Serves as a base for creating
     * statistics based on the data.
     * @param eventList the list containing the events that the statistics should be based on.
     * @param tagHandler the taghandler containing the tags the statistics should be based on
     * @return the hashmap containing the data.
     */
    public static HashMap<ITag, Integer> getEventDelegation(EventList eventList, TagHandler tagHandler) {
        HashMap<ITag, Integer> res = new HashMap<>();
        for (ITag tag : tagHandler.getAllTags()) {
            res.put(tag, 0);
        }

        for (Event event : eventList.getList()) {
            res.replace(event.getTag(), res.get(event.getTag()) + 1);
        }

        return res;
    }

}
