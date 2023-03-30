package seedu.meal360;

import java.util.HashMap;
import java.time.LocalDate;

public class IngredientList extends HashMap<String, Ingredient> {
    private static final Parser parser = new Parser();

    // public method to find count of ingredient
    public Integer findIngredientCount(String ingredientName) {
        try {
            if (this.containsKey(ingredientName)) {
                return this.get(ingredientName).ingredientCount;
            }
            throw new Exceptions.IngredientNotFoundException();
        } catch (Exceptions.IngredientNotFoundException e) {
            return null;
        }
    }

    // public method to find expiry date of ingredient
    public LocalDate findExpiryDate(String ingredientName) {
        try {
            if (this.containsKey(ingredientName)) {
                return this.get(ingredientName).expiryDate;
            }
            throw new Exceptions.IngredientNotFoundException();
        } catch (Exceptions.IngredientNotFoundException e) {
            return null;
        }
    }

    public void addIngredient(Ingredient ingredient) {
        super.put(ingredient.ingredientName, ingredient);
    }

    public void editIngredient(Ingredient ingredient, Integer ingredientCount, String expiryDate) {
        try {
            if (this.containsKey(ingredient.ingredientName)) {
                ingredient.ingredientCount = ingredientCount;
                ingredient.expiryDate = parser.parseDate(expiryDate);
                ingredient.expired = ingredient.expiryDate.isBefore(LocalDate.now());
                super.put(ingredient.ingredientName, ingredient);
            }
            throw new Exceptions.IngredientNotFoundException();
        } catch (Exceptions.IngredientNotFoundException e) {
            System.out.println("Ingredient not found");
        }
    }

    public void deleteIngredient(String ingredient, Integer ingredientCount) {
        try {
            if (this.containsKey(ingredient)) {
                int currentCount = this.get(ingredient).ingredientCount;
                int newCount = currentCount - ingredientCount;
                if (newCount > 0) {
                    this.get(ingredient).ingredientCount = newCount;
                } else {
                    this.remove(ingredient);
                }
            }
            throw new Exceptions.IngredientNotFoundException();
        } catch (Exceptions.IngredientNotFoundException e) {
            System.out.println("Ingredient not found");
        }
    }

    // private indexed list of ingredients, count and expiry date
    private String listIngredients() {
        String ingredientList = "";
        int index = 1;
        for (Ingredient ingredient : this.values()) {
            ingredientList += String.format("%d. %s (count: %d, expires on %s)", index,
                    ingredient.ingredientName, ingredient.ingredientCount, ingredient.expiryDate);
        }
        return ingredientList;
    }

    // public method to print ingredients in list with indexing
    public void printIngredients() {
        System.out.println(listIngredients());
    }

    // public method to print expired ingredients and expiry date
    // if no expired ingredients, print "No expired ingredients"
    public void printExpiredIngredients() {
        String expiredIngredients = "";
        int index = 1;
        for (Ingredient ingredient : this.values()) {
            if (ingredient.expired) {
                expiredIngredients += String.format("%d. %s (expires on %s)", index,
                        ingredient.ingredientName, ingredient.expiryDate);
            }
        }
        if (expiredIngredients.equals("")) {
            System.out.println("No expired ingredients");
        } else {
            System.out.println(expiredIngredients);
        }
    }

    // public method to clear all ingredients
    public void clearIngredients() {
        this.clear();
    }

    // public method to clear expired ingredients
    public void clearExpiredIngredients() {
        for (Ingredient ingredient : this.values()) {
            if (ingredient.expired) {
                this.remove(ingredient.ingredientName);
            }
        }
    }
}
