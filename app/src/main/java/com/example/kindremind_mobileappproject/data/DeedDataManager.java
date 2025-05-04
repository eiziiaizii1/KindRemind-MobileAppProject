package com.example.kindremind_mobileappproject.data;

import com.example.kindremind_mobileappproject.model.CompletedDeed;
import com.example.kindremind_mobileappproject.model.Deed;
import com.example.kindremind_mobileappproject.ui.adapters.CompletedDeedWithDetails;

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

    // In-memory storage for deeds
    private final Map<Integer, Deed> deedMap;
    private final List<CompletedDeed> completedDeeds;

    // Sample deed texts by category for resolving deed details
    private final Map<String, List<String>> deedTextsByCategory;

    private DeedDataManager() {
        deedMap = new HashMap<>();
        completedDeeds = new ArrayList<>();
        deedTextsByCategory = new HashMap<>();

        // Initialize with some default deeds
        initializeDefaultDeeds();
    }

    public static synchronized DeedDataManager getInstance() {
        if (instance == null) {
            instance = new DeedDataManager();
        }
        return instance;
    }

    private void initializeDefaultDeeds() {
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
    public void saveCompletedDeed(CompletedDeed completedDeed) {
        completedDeeds.add(completedDeed);
    }

    /**
     * Get all completed deeds with their details
     */
    public List<CompletedDeedWithDetails> getCompletedDeedsWithDetails() {
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
    }

    /**
     * Add sample completed deeds for testing
     */
    public void addSampleCompletedDeeds() {
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

    /**
     * Get a deed by ID
     */
    public Deed getDeedById(int id) {
        return deedMap.get(id);
    }
}