package seedu.meal360.storage;

import java.io.IOException;
import java.util.HashMap;
import seedu.meal360.IngredientList;
import seedu.meal360.Recipe;
import seedu.meal360.RecipeList;
import seedu.meal360.WeeklyPlan;

public class Database {

    private static final String recipesDatabaseFilepath =
            "." + System.getProperty("file.separator") + "database" + System.getProperty("file.separator")
                    + "recipesDatabase.json";

    private static final String weeklyPlanDatabaseFilepath =
            "." + System.getProperty("file.separator") + "database" + System.getProperty("file.separator")
                    + "weeklyPlanDatabase.json";

    private static final String userIngredientsDatabaseFilepath =
            "." + System.getProperty("file.separator") + "database" + System.getProperty(
                    "file.separator") + "userIngredientsDatabase.json";

    JsonDatabaseHelper<RecipeList> recipeListJsonDatabaseHelper = new JsonDatabaseHelper<>(
            recipesDatabaseFilepath, defaultRecipeList(), RecipeList.class);

    JsonDatabaseHelper<WeeklyPlan> weeklyPlanJsonDatabaseHelper = new JsonDatabaseHelper<>(
            weeklyPlanDatabaseFilepath, new WeeklyPlan(), WeeklyPlan.class);

    JsonDatabaseHelper<IngredientList> userIngredientsJsonDatabaseHelper = new JsonDatabaseHelper<>(
            userIngredientsDatabaseFilepath, new IngredientList(), IngredientList.class);

    public RecipeList loadRecipesDatabase() throws IOException {
        return recipeListJsonDatabaseHelper.loadDatabase();
    }

    public void saveRecipesDatabase(RecipeList recipeList) throws IOException {
        recipeListJsonDatabaseHelper.saveDatabase(recipeList);
    }

    public WeeklyPlan loadWeeklyPlanDatabase() throws IOException {
        return weeklyPlanJsonDatabaseHelper.loadDatabase();
    }

    public void saveWeeklyPlanDatabase(WeeklyPlan weeklyPlan) throws IOException {
        weeklyPlanJsonDatabaseHelper.saveDatabase(weeklyPlan);
    }

    public IngredientList loadUserIngredientsDatabase() throws IOException {
        return userIngredientsJsonDatabaseHelper.loadDatabase();
    }

    public void saveIngredientsDatabase(IngredientList userIngredients) throws IOException {
        userIngredientsJsonDatabaseHelper.saveDatabase(userIngredients);
    }

    public RecipeList defaultRecipeList() {
        RecipeList defaultRecipeList = new RecipeList();
        defaultRecipeList.add(new Recipe("Chicken Rice", new HashMap<>() {
            {
                put("Chicken", 1);
                put("Rice", 1);
                put("Salt", 1);
                put("Pepper", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Tomato fritters", new HashMap<>() {
            {
                put("Ripe mixed-colour cherry tomatoes ", 250);
                put("Flat-leaf parsley", 15);
                put("Garlic cloves", 2);
                put("Plain flour", 2);
                put("Eggs", 2);
                put("Breadcrumbs", 100);
                put("Vegetable oil", 2);
                put("Feta cheese", 30);
            }
        }));

        defaultRecipeList.add(new Recipe("Chicken and mushroom pie", new HashMap<>() {
            {
                put("Chicken breast fillets", 2);
                put("Mushrooms", 250);
                put("Onion", 1);
                put("Plain flour", 2);
                put("Butter", 2);
                put("Milk", 2);
                put("Puff pastry", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Seafood paella", new HashMap<>() {
            {
                put("Prawns", 500);
                put("Mussels", 500);
                put("Chorizo", 100);
                put("Onion", 1);
                put("Garlic cloves", 2);
                put("Rice", 1);
                put("Cherry tomatoes", 250);
                put("Vegetable oil", 2);
                put("Chicken stock", 1);
                put("Saffron", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Avocado toast", new HashMap<>() {
            {
                put("Avocado", 1);
                put("Bread", 2);
                put("Lemon", 1);
                put("Salt", 1);
                put("Pepper", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Italian sausage orzo soup", new HashMap<>() {
            {
                put("Italian sausage", 1);
                put("Onion", 1);
                put("Garlic cloves", 2);
                put("Carrot", 1);
                put("Celery", 1);
                put("Dried oregano", 1);
                put("Dried basil", 1);
                put("Dried thyme", 1);
                put("Dried parsley", 1);
                put("Dried rosemary", 1);
                put("Chicken stock", 1);
                put("Orzo", 1);
                put("Parmesan cheese", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Chicken and mushroom pasta", new HashMap<>() {
            {
                put("Chicken breast fillets", 2);
                put("Mushrooms", 250);
                put("Onion", 1);
                put("Garlic cloves", 2);
                put("Plain flour", 2);
                put("Butter", 2);
                put("Milk", 2);
                put("Pasta", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Creamy Hummus Pasta", new HashMap<>() {
            {
                put("Pasta", 1);
                put("Hummus", 1);
                put("Garlic cloves", 2);
                put("Lemon", 1);
                put("Salt", 1);
                put("Pepper", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Ground beef and potato casserole", new HashMap<>() {
            {
                put("Ground beef", 1);
                put("Potato", 1);
                put("Onion", 1);
                put("Garlic cloves", 2);
                put("Tomato paste", 1);
                put("Tomato sauce", 1);
                put("Beef stock", 1);
                put("Cheddar cheese", 1);
            }
        }));

        defaultRecipeList.add(new Recipe("Buffalo Chicken Mac 'n' Cheese", new HashMap<>() {
            {
                put("Chicken breast fillets", 2);
                put("Macaroni", 1);
                put("Butter", 2);
                put("Flour", 2);
                put("Milk", 2);
                put("Cheddar cheese", 1);
                put("Blue cheese", 1);
                put("Hot sauce", 1);
            }
        }));

        return defaultRecipeList;
    }
}

