package seedu.meal360;

import java.util.HashMap;
import java.util.HashSet;

public class Recipe {

    // changed ingredients to public to edit via editRecipe
    public HashMap<String, Integer> ingredients;
    private HashSet<String> tags;
    private String name;

    public Recipe(String name, HashMap<String, Integer> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
        this.tags = new HashSet <String>();
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getIngredients() {
        return ingredients;
    }

    public int getNumOfIngredients() {
        return ingredients.size();
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(String tag) {
        this.tags.add(tag);
    }
}
