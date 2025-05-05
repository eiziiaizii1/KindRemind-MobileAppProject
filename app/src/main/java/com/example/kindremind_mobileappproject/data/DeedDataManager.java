package com.example.kindremind_mobileappproject.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.example.kindremind_mobileappproject.model.CompletedDeed;
import com.example.kindremind_mobileappproject.model.Deed;
import com.example.kindremind_mobileappproject.ui.adapters.CompletedDeedWithDetails;
import com.example.kindremind_mobileappproject.ui.adapters.DBAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Temporary in-memory manager for deed data
 * Will be replaced by a proper repository connected to Room database
 */
public class DeedDataManager {

    // Singleton instance
    private static DeedDataManager instance;
    private DeedApiManager apiManager;

    // In-memory storage for deeds
    private final Map<Integer, Deed> deedMap;
    //private final List<CompletedDeed> completedDeeds;

    // Sample deed texts by category for resolving deed details
    private final Map<String, List<String>> deedTextsByCategory;
    private Runnable readyCallback;

    private DBAdapter dbAdapter;

    private DeedDataManager(Context context) {
        deedMap = new HashMap<>();
        //completedDeeds = new ArrayList<>();
        deedTextsByCategory = new HashMap<>();
        apiManager = DeedApiManager.getInstance();
        dbAdapter = DBAdapter.getInstance(context);

        // Initialize with some default deeds
        //initializeDefaultDeeds();
        initializeDeedsFromApi();
    }

    public static synchronized DeedDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DeedDataManager(context);
        }
        return instance;
    }

   /* private void initializeDefaultDeeds() {
        // Add sample deeds by category
        List<String> environmentDeeds = new ArrayList<>();
        environmentDeeds.add("Unplug one idle device to save energy.");
        environmentDeeds.add("Pick up litter in your local park.");
        environmentDeeds.add("Use a reusable water bottle today.");
        environmentDeeds.add("Plant a native flower in your garden.");
        deedTextsByCategory.put("environment", environmentDeeds);

        List<String> empathyDeeds = new ArrayList<>();
        empathyDeeds.add("Compliment a stranger today.");
        empathyDeeds.add("Listen fully when someone is speaking to you.");
        empathyDeeds.add("Ask someone how they are feeling and truly listen.");
        empathyDeeds.add("Write a thank you note to someone who helped you.");
        deedTextsByCategory.put("empathy", empathyDeeds);

        List<String> communityDeeds = new ArrayList<>();
        communityDeeds.add("Offer to help an elderly neighbor with groceries.");
        communityDeeds.add("Donate unused items to a local charity.");
        communityDeeds.add("Support a local small business today.");
        communityDeeds.add("Hold the door open for someone.");
        deedTextsByCategory.put("community", communityDeeds);

        List<String> healthDeeds = new ArrayList<>();
        healthDeeds.add("Try a new healthy recipe for dinner.");
        healthDeeds.add("Take a 15-minute walk outside.");
        healthDeeds.add("Practice deep breathing for 5 minutes.");
        healthDeeds.add("Drink an extra glass of water today.");
        deedTextsByCategory.put("health", healthDeeds);

        // Add some deeds to the deed map
        addSampleDeedsToMap();
    }*/

    private void initializeDeedsFromApi() {
        apiManager.getAllDeedsFromJSON("https://run.mocky.io/v3/8c0c776d-6dfe-432f-a69f-a56c36501e00/", allDeeds -> {
            List<String> environmentDeeds = new ArrayList<>();
            List<String> empathyDeeds = new ArrayList<>();
            List<String> communityDeeds = new ArrayList<>();
            List<String> healthDeeds = new ArrayList<>();

            for (String text : allDeeds.keySet()) {
                String category = allDeeds.get(text);
                switch (category) {
                    case "environment": environmentDeeds.add(text); break;
                    case "empathy": empathyDeeds.add(text); break;
                    case "community": communityDeeds.add(text); break;
                    case "health": healthDeeds.add(text); break;
                }
            }

            if (!environmentDeeds.isEmpty()) deedTextsByCategory.put("environment", environmentDeeds);
            if (!empathyDeeds.isEmpty()) deedTextsByCategory.put("empathy", empathyDeeds);
            if (!communityDeeds.isEmpty()) deedTextsByCategory.put("community", communityDeeds);
            if (!healthDeeds.isEmpty()) deedTextsByCategory.put("health", healthDeeds);

            addSampleDeedsToMap();

            //  tell MainActivity the data exists
            if (readyCallback != null)  new Handler(Looper.getMainLooper()).post(readyCallback);

        });
    }

    private void addSampleDeedsToMap() {
        int id = 1;
        for (String category : deedTextsByCategory.keySet()) {
            List<String> texts = deedTextsByCategory.get(category);
            for (String text : texts) {
                deedMap.put(id, new Deed(id, category, text));
                id++;
            }
        }
    }

    /**
     * Save a completed deed to in-memory storage
     */

    /*
    public void saveCompletedDeed(CompletedDeed completedDeed) {
        completedDeeds.add(completedDeed);
    }
    */
    public void saveCompletedDeed(CompletedDeed completedDeed){
        dbAdapter.insertCompletedDeed(completedDeed.getDeedId(), completedDeed.getCustomIntValue(),completedDeed.getDate(),completedDeed.getNote());
    }

    /**
     * Get all completed deeds with their details
     */
    /*public List<CompletedDeedWithDetails> getCompletedDeedsWithDetails() {
        List<CompletedDeedWithDetails> result = new ArrayList<>();

        for (CompletedDeed completedDeed : completedDeeds) {
            // Find the associated deed
            Deed deed = deedMap.get(completedDeed.getDeedId());

            String deedText;
            String category;

            if (deed != null) {
                deedText = deed.getText();
                category = deed.getCat();
            } else {
                // If deed not found, provide defaults (could happen with custom deeds)
                deedText = "Custom deed";
                category = "other";
            }

            CompletedDeedWithDetails item = new CompletedDeedWithDetails(
                    completedDeed.getPk(),
                    deedText,
                    category,
                    completedDeed.getDate(),
                    completedDeed.getNote(),
                    completedDeed.isCustom()
            );

            result.add(item);
        }

        return result;
    } */

    public List<CompletedDeedWithDetails> getCompletedDeedsWithDetails(){

        List<CompletedDeedWithDetails> result = new ArrayList<>();

        Cursor c = dbAdapter.getAllCompletedDeeds();

        if (c != null && c.moveToFirst()){

            int completedDeedCount = c.getCount();

            for (int i = 0;i<completedDeedCount;i++){
                // Find the associated deed
                Deed deed = deedMap.get(c.getInt(1));

                String deedText;
                String category;

                if (deed != null) {
                    deedText = deed.getText();
                    category = deed.getCat();
                } else {
                    // If deed not found, provide defaults (could happen with custom deeds)
                    deedText = "Custom deed";
                    category = "other";
                }

                boolean custom = false;
                if (c.getInt(2) == 1)
                    custom = true;

                CompletedDeedWithDetails item = new CompletedDeedWithDetails(
                        c.getInt(0),
                        deedText,
                        category,
                        c.getString(3),
                        c.getString(4),
                        custom
                );

                result.add(item);
                c.moveToNext();
            }
        }
        return result;
    }

    /**
     * Add sample completed deeds for testing
     */
   /* public void addSampleCompletedDeeds() {
        // Only add samples if the list is empty
//        if (completedDeeds.isEmpty()) {
//            CompletedDeed deed1 = new CompletedDeed(1, false, "2025-05-01", null);
//            deed1.setPk(1);
//            CompletedDeed deed2 = new CompletedDeed(5, false, "2025-05-02", "I really enjoyed this!");
//            deed2.setPk(2);
//            CompletedDeed deed3 = new CompletedDeed(9, false, "2025-05-03", null);
//            deed3.setPk(3);
//
//            completedDeeds.add(deed1);
//            completedDeeds.add(deed2);
//            completedDeeds.add(deed3);
//        }
    }
*/
    /**
     * Get a deed by ID
     */
    public Deed getDeedById(int id) {
        return deedMap.get(id);
    }

    public int getDeedCount(){
        return deedMap.size();
    }

    public void whenReady(Runnable r) {
        if (!deedMap.isEmpty()) {          // data already fetched
            r.run();
        } else {
            readyCallback = r;             // remember it for later
        }
    }
}