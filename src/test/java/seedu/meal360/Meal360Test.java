package seedu.meal360;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.meal360.exceptions.InvalidNegativeValueException;
import seedu.meal360.exceptions.InvalidRecipeNameException;
import seedu.meal360.storage.Database;


class Meal360Test {

    private static RecipeList recipes = new RecipeList();
    private static final Parser parser = new Parser();
    private static final Ui ui = new Ui();

    private static final Database database = new Database();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeEach
    public void setUpRecipes() {
        // Adding of recipes
        HashMap<String, Integer> burgerIngredients = new HashMap<>();
        burgerIngredients.put("buns", 2);
        burgerIngredients.put("meat patty", 1);
        burgerIngredients.put("lettuce", 3);
        Recipe burger = new Recipe("burger", burgerIngredients);

        HashMap<String, Integer> pizzaIngredients = new HashMap<>();
        pizzaIngredients.put("dough", 1);
        pizzaIngredients.put("tomato sauce", 1);
        pizzaIngredients.put("cheese", 1);
        pizzaIngredients.put("pepperoni", 1);
        Recipe pizza = new Recipe("pizza", pizzaIngredients);

        HashMap<String, Integer> saladIngredients = new HashMap<>();
        saladIngredients.put("lettuce", 1);
        saladIngredients.put("tomato", 1);
        saladIngredients.put("cucumber", 1);
        Recipe salad = new Recipe("salad", saladIngredients);

        recipes.addRecipe(burger);
        recipes.addRecipe(pizza);
        recipes.addRecipe(salad);
    }

    @AfterEach
    public void tearDownRecipes() {
        recipes = new RecipeList();
    }

    @Test
    public void testViewRecipe() {
        Recipe recipe = parser.parseViewRecipe(new String[]{"view", "1"}, recipes);
        assertEquals("burger", recipe.getName());
        assertEquals(2, (int) recipe.getIngredients().get("buns"));
        assertEquals(1, (int) recipe.getIngredients().get("meat patty"));
        assertEquals(3, (int) recipe.getIngredients().get("lettuce"));

        // Testing exceptions
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> parser.parseViewRecipe(new String[]{"view"}, recipes));

        assertThrows(NumberFormatException.class,
                () -> parser.parseViewRecipe(new String[]{"view", "a"}, recipes));

        assertThrows(IndexOutOfBoundsException.class,
                () -> parser.parseViewRecipe(new String[]{"view", "5"}, recipes));

        assertThrows(IndexOutOfBoundsException.class,
                () -> parser.parseViewRecipe(new String[]{"view", "0"}, recipes));
    }

    @Test
    public void testDeleteRecipe() {
        HashMap<String, Integer> testIngredients = new HashMap<>();
        testIngredients.put("test ingredient", 100);
        // create a fake recipe
        Recipe testR = new Recipe("test recipe name", testIngredients);
        recipes.addRecipe(testR);

        int oldRecipeListSize = recipes.size();
        // delete recipe
        recipes.deleteRecipe(recipes.indexOf(testR));
        int newRecipeListSize = recipes.size();
        // check if recipe list size is 1 less than before
        assertEquals((oldRecipeListSize - 1), newRecipeListSize);
    }

    @Test
    public void testAddWeeklyPlan() throws InvalidRecipeNameException, InvalidNegativeValueException {
        WeeklyPlan weeklyPlan = new WeeklyPlan();

        // Testing add recipe to weekly plan
        WeeklyPlan recipeMap = parser.parseWeeklyPlan(new String[]{"weekly", "/add", "burger", "1"}, recipes);
        weeklyPlan.addPlans(recipeMap);
        assertTrue(weeklyPlan.containsKey("burger"));
        assertEquals(1, (int) weeklyPlan.get("burger"));
        assertFalse(weeklyPlan.containsKey("pizza"));

        recipeMap = parser.parseWeeklyPlan(new String[]{"weekly", "/add", "pizza", "3"}, recipes);
        weeklyPlan.addPlans(recipeMap);
        assertTrue(weeklyPlan.containsKey("pizza"));
        assertEquals(3, (int) weeklyPlan.get("pizza"));

        // Testing throwing of exceptions
        assertThrows(IllegalArgumentException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "burger", "1"}, recipes));

        assertThrows(IllegalArgumentException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "burger"}, recipes));

        assertThrows(NumberFormatException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "/add", "burger"}, recipes));

        assertThrows(NumberFormatException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "/add", "burger", "a"}, recipes));

        assertThrows(InvalidNegativeValueException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "/add", "burger", "0"}, recipes));

        assertThrows(InvalidNegativeValueException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "/add", "burger", "-9"}, recipes));

        assertThrows(InvalidRecipeNameException.class,
                () -> parser.parseWeeklyPlan(new String[]{"weekly", "/add", "unknown", "1"}, recipes));

        // TODO: Check error messages
    }

    @Test
    public void testAddMultiWeeklyPlan() throws InvalidRecipeNameException, InvalidNegativeValueException {
        WeeklyPlan weeklyPlan = new WeeklyPlan();

        // Testing add recipe to weekly plan
        WeeklyPlan recipeMap = parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burger", "/q", "1", "/r", "pizza", "/q", "5"},
                recipes);
        weeklyPlan.addPlans(recipeMap);
        assertTrue(weeklyPlan.containsKey("burger"));
        assertEquals(1, (int) weeklyPlan.get("burger"));
        assertTrue(weeklyPlan.containsKey("pizza"));
        assertEquals(5, (int) weeklyPlan.get("pizza"));
        assertFalse(weeklyPlan.containsKey("salad"));

        // Testing exceptions
        assertThrows(InvalidRecipeNameException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "1", "/r", "pizza", "/q", "5"},
                recipes));

        assertThrows(InvalidNegativeValueException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "0", "/r", "pizza", "/q", "5"},
                recipes));

        assertThrows(InvalidNegativeValueException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "-1", "/r", "pizza", "/q", "5"},
                recipes));

        assertThrows(NumberFormatException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "one", "/r", "pizza", "/q", "5"},
                recipes));

        assertThrows(IllegalArgumentException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "1", "/r", "salad", "/r", "pizza"},
                recipes));

        assertThrows(IllegalArgumentException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multiadd", "/r", "burgers", "/q", "1", "/r", "salad", "/q", "/q"},
                recipes));
    }

    @Test
    public void testDeleteWeeklyPlan() {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.put("salad", 1);
        weeklyPlan.put("pizza", 3);

        // Testing delete recipe from weekly plan
        int oldWeeklyPlanSize = weeklyPlan.size();
        WeeklyPlan toDelete = new WeeklyPlan();

        // Testing negative case
        toDelete.put("burger", 0);
        assertThrows(IllegalArgumentException.class, () -> weeklyPlan.deletePlans(toDelete));

        // Testing positive case
        int newWeeklyPlanSize = weeklyPlan.size();
        assertEquals(oldWeeklyPlanSize, newWeeklyPlanSize);
        toDelete.clear();
        toDelete.put("salad", 0);
        weeklyPlan.deletePlans(toDelete);
        newWeeklyPlanSize = weeklyPlan.size();
        assertEquals(oldWeeklyPlanSize - 1, newWeeklyPlanSize);
    }

    @Test
    public void testDeleteMultiWeeklyPlan() throws InvalidRecipeNameException, InvalidNegativeValueException {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.put("salad", 1);
        weeklyPlan.put("pizza", 3);
        weeklyPlan.put("burger", 100);

        // Testing delete recipe from weekly plan
        WeeklyPlan recipeMap = parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "/r", "burger", "/r", "pizza"}, recipes);
        weeklyPlan.deletePlans(recipeMap);
        assertEquals(1, weeklyPlan.size());

        // Testing exceptions
        weeklyPlan.put("pizza", 3);
        weeklyPlan.put("burger", 100);
        assertThrows(InvalidRecipeNameException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "/r", "burgers", "/r", "pizza"}, recipes));

        assertThrows(IllegalArgumentException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "burger", "pizza", "salad"}, recipes));

        assertThrows(InvalidRecipeNameException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "/r", "burger", "/q", "1", "/r", "pizza"}, recipes));
    }

    @Test
    public void testClearWeeklyPlan() {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.put("salad", 1);
        weeklyPlan.put("pizza", 3);

        // Testing clearing weekly plan
        weeklyPlan.clearPlan();
        assertEquals(weeklyPlan.size(), 0);
    }

    @Test
    public void testViewIngredients() {

        HashMap<String, Integer> pastaIngredients = new HashMap<>();
        pastaIngredients.put("penne", 1);
        Recipe pasta = new Recipe("pasta", pastaIngredients);
        recipes.add(pasta);

        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.put("pasta", 1);

        ui.printWeeklyIngredients(weeklyPlan, recipes);
        assertEquals(ui.formatMessage("Here are your weekly ingredients:") + System.lineSeparator()
                + ui.formatMessage("penne (1)"), outContent.toString().trim());
    }

    @Test
    public void testViewEmptyWeeklyPlan() {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        ui.printWeeklyPlan(weeklyPlan);
        assertEquals(ui.formatMessage("Your weekly plan is empty!"), outContent.toString().trim());
    }

    @Test
    public void testViewNonEmptyWeeklyPlan() {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.put("salad", 1);
        ui.printWeeklyPlan(weeklyPlan);
        assertEquals(
                ui.formatMessage("Here is your weekly plan:") + System.lineSeparator() + ui.formatMessage(
                        "salad x1"), outContent.toString().trim());
    }

    @Test
    public void testListRecipe() {
        RecipeList recipeListToPrint;
        String[] inputs;

        inputs = new String[]{"list"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(3, recipeListToPrint.size());
        assertEquals(3, recipeListToPrint.get(0).getNumOfIngredients());
        assertEquals(4, recipeListToPrint.get(1).getNumOfIngredients());
        assertEquals(3, recipeListToPrint.get(2).getNumOfIngredients());

        inputs = new String[]{"list", "tomato", "sauce"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(1, recipeListToPrint.size());
        assertEquals(4, recipeListToPrint.get(0).getNumOfIngredients());
        assertEquals("pizza", recipeListToPrint.get(0).getName());

        inputs = new String[]{"list", "salad", "&", "tomato"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(1, recipeListToPrint.size());
        assertEquals(3, recipeListToPrint.get(0).getNumOfIngredients());
        assertEquals("salad", recipeListToPrint.get(0).getName());

        inputs = new String[]{"list", "salad", "&", "pizza"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(0, recipeListToPrint.size());
    }

    @Test
    public void testLoadDatabase() {
        assertDoesNotThrow(database::loadRecipesDatabase);
    }
}
