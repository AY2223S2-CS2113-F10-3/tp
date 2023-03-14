package seedu.meal360;

import java.util.HashMap;
import java.util.Scanner;

public class Meal360 {

    /**
     * Main entry-point for the java.duke.Meal360 application.
     */

    private static Boolean canExit = false;
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();
    private static final Database database = new Database();
    private static final RecipeList recipeList = new RecipeList();
    private static final WeeklyPlan weeklyPlan = new WeeklyPlan();

    public static void startApp() {
        ui.printWelcomeMessage();
        // Dummy recipe for testing purposes
        HashMap<String, Integer> testIngredients = new HashMap<>();
        testIngredients.put("test ingredient", 100);
        Recipe testR = new Recipe("test recipe name", testIngredients);
        recipeList.addRecipe(testR);
    }

    public static void receiveInput(String input) {
        String[] command = input.trim().split(" ");
        if (input.equalsIgnoreCase("bye")) {
            canExit = true;
        } else if (command[0].equals("delete")) {
            ui.printSeparator();
            try {
                Recipe deletedRecipe = parser.parseDeleteRecipe(command, recipeList);
                ui.printMessage("Noted. I've removed this recipe:");
                ui.printMessage(deletedRecipe.getName());
                ui.printMessage("Now you have " + recipeList.size() + " recipes in the list.");
            } catch (ArrayIndexOutOfBoundsException e) {
                String errorMessage = String.format(
                        "Please enter a valid recipe number or name. You did not enter a recipe number or name.");
                ui.printMessage(errorMessage);
            } catch (IndexOutOfBoundsException e) {
                String errorMessage = String.format(
                        "Please enter a valid recipe number or mame. You entered %s, " + "which is in ivalid.",
                        command[1]);
                ui.printMessage(errorMessage);
            }
            ui.printSeparator();
        } else if (command[0].equals("view")) {
            ui.printSeparator();
            try {
                Recipe recipe = parser.parseViewRecipe(command, recipeList);
                ui.printRecipe(recipe);
            } catch (NumberFormatException e) {
                String errorMessage = String.format(
                        "Please enter a valid recipe number. You entered %s, " + "which is not a number.",
                        command[1]);
                ui.printMessage(errorMessage);
            } catch (ArrayIndexOutOfBoundsException e) {
                String errorMessage = String.format(
                        "Please enter a valid recipe number. You did not enter a recipe number.");
                ui.printMessage(errorMessage);
            } catch (IndexOutOfBoundsException e) {
                String errorMessage = String.format(
                        "Please enter a valid recipe number. You entered %s, " + "which is out of bounds.",
                        command[1]);
                ui.printMessage(errorMessage);
            }
            ui.printSeparator();
        } else if (command[0].equals("list")) {
            RecipeList recipeListToPrint = parser.parseListRecipe(command, recipeList);
            ui.listRecipe(recipeListToPrint);
        } else if (command[0].equals("add")) {
            ui.printSeparator();
            Recipe newRecipe = parser.parseAddRecipe(command, recipeList);
            ui.printMessage("I've added this new recipe:" + newRecipe.getName());
            ui.printMessage("Now you have " + recipeList.size() + " recipes in the list.");
            ui.printSeparator();
        } else if (command[0].equals("edit")) {
            ui.printSeparator();
            Recipe newRecipe = parser.parseEditRecipe(command, recipeList);
            ui.printMessage("I've edited this recipe:" + newRecipe.getName());
            ui.printSeparator();
        } else if (command[0].equals("weekly")) {
            try {
                ui.printSeparator();
                WeeklyPlan recipeMap = parser.parseWeeklyPlan(command, recipeList);

                if (command[1].equals("/add")) {
                    ui.printMessage("I've added the recipe to your weekly plan!");
                    weeklyPlan.addPlan(recipeMap);
                } else if (command[1].equals("/delete")) {
                    ui.printMessage("I've deleted the recipe from your weekly plan!");
                    weeklyPlan.deletePlan(recipeMap);
                }
            } catch (NumberFormatException e) {
                String errorMessage = String.format("Please enter a valid number as the last argument.");
                ui.printMessage(errorMessage);
            } catch (IllegalArgumentException e) {
                ui.printMessage(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                String errorMessage = String.format("Insufficient number of arguments provided.");
                ui.printMessage(errorMessage);
            }
            ui.printSeparator();
        } else if (command[0].equals("weeklyplan")) {
            ui.printSeparator();
            ui.printWeeklyPlan(weeklyPlan);
            ui.printSeparator();
        } else if (command[0].equals("help")) {
            ui.printSeparator();
            ui.printHelp();
            ui.printSeparator();
        } else {
            ui.printSeparator();
            ui.printMessage("I'm sorry, but I don't know what that means :-(");
            ui.printSeparator();
        }
    }

    public static void exitApp() {
        ui.printGoodbyeMessage();
    }

    public static void main(String[] args) {
        startApp();
        String line;
        Scanner userInput = new Scanner(System.in);

        do {
            line = userInput.nextLine();
            receiveInput(line);
        } while (!canExit);

        exitApp();
    }
}
