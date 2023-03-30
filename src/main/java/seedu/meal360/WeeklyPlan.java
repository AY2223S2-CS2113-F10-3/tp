package seedu.meal360;

import java.util.ArrayList;
import java.util.HashMap;

public class WeeklyPlan extends HashMap<String, Integer> {

    public void addPlans(WeeklyPlan recipeMap) {
        recipeMap.forEach((recipe, count) -> {
            if (this.containsKey(recipe)) {
                this.put(recipe, this.get(recipe) + count); // Case when recipe already exists in plan
            } else {
                this.put(recipe, count);
            }
        });
    }

    public void deletePlans(WeeklyPlan recipeMap) {
        recipeMap.forEach((recipe, count) -> {
            if (this.containsKey(recipe)) {
                int currentCount = this.get(recipe);
                int newCount = currentCount - count;
                this.remove(recipe);
                if (newCount > 0) {
                    this.put(recipe, newCount);
                }
            } else {
                throw new IllegalArgumentException("Recipe does not exist in weekly plan!");
            }
        });
    }

    public void clearPlan() {
        this.clear();
    }

    public boolean checkValidity(RecipeList recipeList) {
        ArrayList<String> toRemove = new ArrayList<>();
        this.forEach((recipe, count) -> {
            if (recipeList.findByName(recipe) == null) {
                toRemove.add(recipe);
            }
        });

        if (toRemove.size() == 0) {
            return true;
        }
        toRemove.forEach(recipe -> this.remove(recipe));
        return false;
    }
}
