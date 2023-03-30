package seedu.meal360;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.meal360.exceptions.InvalidNegativeValueException;
import seedu.meal360.exceptions.InvalidRecipeNameException;
import seedu.meal360.storage.Database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        toDelete.put("salad", 1);
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
                new String[]{"weekly", "/multidelete", "/r", "burger", "/q", "1", "/r", "pizza", "/q", "1"},
                recipes);
        weeklyPlan.deletePlans(recipeMap);
        assertEquals(3, weeklyPlan.size());

        // Testing exceptions
        weeklyPlan.put("pizza", 3);
        weeklyPlan.put("burger", 100);
        assertThrows(InvalidRecipeNameException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "/r", "burgers", "/q", "2"}, recipes));

        assertThrows(IllegalArgumentException.class, () -> parser.parseWeeklyPlan(
                new String[]{"weekly", "/multidelete", "burger", "pizza", "salad"}, recipes));

        assertThrows(IllegalArgumentException.class, () -> parser.parseWeeklyPlan(
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

        inputs = new String[]{"list", "salad", "&&", "tomato"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(1, recipeListToPrint.size());
        assertEquals(3, recipeListToPrint.get(0).getNumOfIngredients());
        assertEquals("salad", recipeListToPrint.get(0).getName());

        inputs = new String[]{"list", "salad", "&&", "pizza"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(0, recipeListToPrint.size());
    }

    @Test
    public void testListTagAndAddTag() {
        RecipeList recipeListToPrint;
        String[] inputs;
        // add tag for testing
        String[] addTagInputs = new String[]{"tag", "breakfast", "<<", "salad"};
        parser.parseTagRecipe(addTagInputs, recipes);

        inputs = new String[]{"list", "/t", "breakfast"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(recipeListToPrint.size(), 1);
        assertEquals(recipeListToPrint.get(0).getName(), "salad");

        // add tag for testing
        addTagInputs = new String[]{"tag", "breakfast", "<<", "pizza"};
        parser.parseTagRecipe(addTagInputs, recipes);
        addTagInputs = new String[]{"tag", "western", "<<", "burger"};
        parser.parseTagRecipe(addTagInputs, recipes);

        inputs = new String[]{"list", "/t", "breakfast"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(recipeListToPrint.size(), 2);
        assertEquals(recipeListToPrint.get(1).getName(), "pizza");

        inputs = new String[]{"list", "/t", "breakfast", "&&", "western"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(recipeListToPrint.size(), 0);

        // add tag for testing
        addTagInputs = new String[]{"tag", "western", "<<", "pizza"};
        parser.parseTagRecipe(addTagInputs, recipes);
        inputs = new String[]{"list", "/t", "breakfast", "&&", "western"};
        recipeListToPrint = parser.parseListRecipe(inputs, recipes);
        assertEquals(recipeListToPrint.size(), 1);
        assertEquals(recipeListToPrint.get(0).getName(), "pizza");
    }

    @Test
    public void testListTagException() {
        String[] inputs;

        //check exception
        String[] noArgInputs = new String[]{"list", "/t"};
        assertThrows(IllegalArgumentException.class, () -> parser.parseListRecipe(noArgInputs, recipes));

        String[] notFoundTagInputs1 = new String[]{"list", "/t", "/t"};
        assertThrows(NullPointerException.class, () -> parser.parseListRecipe(notFoundTagInputs1, recipes));

        String[] notFoundTagInputs2 = new String[]{"list", "/t", "phone", "&&", "computer"};
        assertThrows(NullPointerException.class, () -> parser.parseListRecipe(notFoundTagInputs2, recipes));

        String[] beforeAddTagInputs = new String[]{"list", "/t", "breakfast"};
        assertThrows(NullPointerException.class, () -> parser.parseListRecipe(beforeAddTagInputs, recipes));

        // add tag for testing
        inputs = new String[]{"tag", "breakfast", "<<", "salad"};
        parser.parseTagRecipe(inputs, recipes);
        inputs = new String[]{"tag", "breakfast", ">>", "salad"};
        parser.parseTagRecipe(inputs, recipes);

        String[] nothingInTagInputs = new String[]{"list", "/t", "breakfast"};
        ui.listRecipe(parser.parseListRecipe(nothingInTagInputs, recipes));
        assertEquals(ui.formatMessage("There is nothing to list."), outContent.toString().trim());
    }

    @Test
    public void testRemoveTagAndAddTag() {
        String[] inputs;
        Recipe invalidRecipe = new Recipe("invalid Recipe", new HashMap<>(1,1));

        inputs = new String[]{"tag", "western", "<<", "burger"};
        parser.parseTagRecipe(inputs, recipes);
        inputs = new String[]{"tag", "western", "<<", "pizza"};
        parser.parseTagRecipe(inputs, recipes);

        //exception
        assertThrows(IndexOutOfBoundsException.class, ()
                -> recipes.removeRecipeFromTag("western", invalidRecipe));
        String[] invalidTagInputs = new String[]{"tag", "random", "tag", ">>", "burger"};
        assertThrows(IndexOutOfBoundsException.class, ()
                -> parser.parseTagRecipe(invalidTagInputs, recipes));

        String[] noRecipeInTagInputs = new String[]{"tag", "breakfast", ">>", "pizza"};
        assertThrows(IndexOutOfBoundsException.class, ()
                -> parser.parseTagRecipe(noRecipeInTagInputs, recipes));

        inputs = new String[]{"tag", "western", ">>", "pizza"};
        parser.parseTagRecipe(inputs, recipes);
        assertEquals(recipes.tags.get("western").size(), 1);
    }

    @Test
    public void testRandomRecipe() {
        // no recipe in the lists
        RecipeList blankRecipeList = new RecipeList();
        assertThrows(NullPointerException.class, () -> parser.parseRandomRecipe(blankRecipeList));


        // general case
        Recipe randomRecipe1 = parser.parseRandomRecipe(recipes);
        assertNotNull(randomRecipe1);
        assertNotNull(recipes.randomRecipe());
    }

    @Test
    public void testLoadDatabase() {
        assertDoesNotThrow(database::loadRecipesDatabase);
    }
}
