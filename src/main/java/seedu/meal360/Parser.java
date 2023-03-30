package seedu.meal360;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import seedu.meal360.exceptions.InvalidNegativeValueException;
import seedu.meal360.exceptions.InvalidRecipeNameException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class Parser {

    Ui ui = new Ui();

    public String combineWords(String[] input, int startIndex, int length) {
        StringBuilder word = new StringBuilder(input[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            word.append(" ").append(input[i]);
        }

        return word.toString();
    }

    public HashMap<String, Integer> parseIngredientName(String[] command) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        int flag = 0;
        StringBuilder add = null;
        String ingredientName;
        int ingredientQuantity;
        int indexOfEqual;

        for (String s : command) {
            try {
                indexOfEqual = s.indexOf("=");
                if (indexOfEqual == -1) {
                    if (add != null) {
                        add.append(" ").append(s);
                    } else {
                        add = new StringBuilder(s);
                    }
                    flag++;

                } else {
                    if (flag > 0) {
                        indexOfEqual = s.indexOf("=");
                        ingredientName = (s.substring(0, indexOfEqual));
                        ingredientName = add + " " + ingredientName;
                        ingredientQuantity = Integer.parseInt(s.substring(indexOfEqual + 1));
                        flag = 0;
                        add = null;
                    } else {
                        ingredientName = s.substring(0, indexOfEqual);
                        ingredientQuantity = Integer.parseInt(s.substring(indexOfEqual + 1));
                    }

                    ingredients.put(ingredientName, ingredientQuantity);

                }
            } catch (NumberFormatException e) {
                ui.printSeparator();
                String errorMessage = "Please enter ingredients properly![eg: chicken=100]";
                ui.printMessage(errorMessage);
                ui.printSeparator();
            }
        }
        return ingredients;
    }

    public Recipe parseAddRecipe(String[] input, RecipeList recipeList) {
        String recipeName = combineWords(input, 2, input.length);
        if (recipeList.findByName(recipeName) != null) {
            return null;
        }
        HashMap<String, Integer> ingredients = new HashMap<>();
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please Enter The Ingredients & Quantity: ");
        while (true) {
            String line = userInput.nextLine();
            if (line.equals("done")) {
                ui.printSeparator();
                break;
            } else {
                String[] command = line.trim().split(" ");
                ingredients = parseIngredientName(command);
            }
        }
        Recipe newRecipe = new Recipe(recipeName, ingredients);
        recipeList.addRecipe(newRecipe);
        return newRecipe;
    }

    public Recipe parseEditRecipe(String[] input, RecipeList recipeList) {
        String recipeName = combineWords(input, 2, input.length);
        Recipe recipeToEdit;
        HashMap<String, Integer> ingredients;
        Scanner userInput = new Scanner(System.in);
        if (recipeList.findByName(recipeName) == null) {
            return null;
        }
        recipeToEdit = recipeList.findByName(recipeName);
        System.out.println("Do you want to edit recipe fully or partially?");
        System.out.println("Press 1 for full edit | Press 2 for partial edit | Press 3 to add ingredients");

        Scanner getInput = new Scanner(System.in);
        int index = getInput.nextInt();
        if (index == 1) {
            System.out.println("Please Enter New Ingredients & Quantity: ");
            while (true) {
                String line = userInput.nextLine();
                if (line.equals("done")) {
                    break;
                }
                String[] command = line.trim().split(" ");
                ingredients = parseIngredientName(command);
                recipeList.editRecipe(recipeToEdit, ingredients);
            }
        } else if (index == 2) {
            System.out.println("These are the ingredients for the recipe:");
            ui.printSeparator();
            Recipe recipe = parseViewRecipe(recipeName, recipeList);
            ui.printRecipe(recipe);
            ui.printSeparator();
            System.out.println("Which ingredient do you want to change?");
            int ingredientIndex = getInput.nextInt();
            ingredientIndex -= 1;
            int count = 0;
            String ingredientToRemove = null;
            for (String ingredient : recipeToEdit.getIngredients().keySet()) {
                if (ingredientIndex == count) {
                    ingredientToRemove = ingredient;
                    System.out.println("Ingredient to be changed:");
                    ui.printSeparator();
                    String toPrint = String.format("%s(%d)", ingredient,
                            recipeToEdit.getIngredients().get(ingredient));
                    System.out.println(ui.formatMessage(toPrint));
                    ui.printSeparator();
                    break;
                }
                count++;
            }
            System.out.println("Please enter the new ingredient:");
            String line = userInput.nextLine();
            String command = line.replaceAll("\\s+", " ");
            int indexOfEqual = command.indexOf("=");
            String newIngredientName = command.substring(0, indexOfEqual);
            int newIngredientQuantity = Integer.parseInt(command.substring(indexOfEqual + 1));
            recipeToEdit.getIngredients().remove(ingredientToRemove);
            recipeToEdit.getIngredients().put(newIngredientName, newIngredientQuantity);
            recipeList.editRecipe(recipeToEdit, recipeToEdit.getIngredients());
        } else if (index == 3) {
            HashMap<String, Integer> newIngredientList = recipeToEdit.getIngredients();
            System.out.println("These are the current ingredients:");
            ui.printSeparator();
            Recipe recipe = parseViewRecipe(recipeName, recipeList);
            ui.printRecipe(recipe);
            ui.printSeparator();
            System.out.println("Please Enter Additional Ingredients & Quantity (Enter done when complete): ");
            while (true) {
                String line = userInput.nextLine();
                if (line.equals("done")) {
                    ui.printSeparator();
                    break;
                } else {
                    String[] command = line.trim().split(" ");
                    ingredients = parseIngredientName(command);
                    newIngredientList.putAll(ingredients);
                }
            }
            recipeList.editRecipe(recipeToEdit, newIngredientList);
        }

        return recipeToEdit;
    }

    public String parseDeleteRecipe(String[] input, RecipeList recipeList) {
        assert input[0].equals("delete");
        // user inputted recipe name
        if (input[1].contains("r/")) {
            // skip over /r in recipe name
            String recipeToDelete = input[1].substring(2);
            if (recipeToDelete.equals("all")) {
                StringBuilder allRecipes = new StringBuilder();
                System.out.println("recipeList size: " + recipeList.size());
                int index = 0;
                while (recipeList.size() != 0) {
                    allRecipes.append(recipeList.deleteRecipe(index).getName()).append(", ");
                }
                allRecipes = new StringBuilder(allRecipes.substring(0, allRecipes.length() - 2));
                return allRecipes.toString();
            } else {
                int recipeIndex = 0;
                for (Recipe recipe : recipeList) {
                    // find index of recipe we want to delete
                    if (recipe.getName().equals(recipeToDelete)) {
                        break;
                    }
                    recipeIndex++;
                }
                return recipeList.deleteRecipe(recipeIndex).getName();
            }
            // user inputted index of recipe in list
        } else {
            // check if deleting a range of recipes
            if (input[1].length() >= 3) {
                StringBuilder rangeRecipes = new StringBuilder();
                int startIndex = Integer.parseInt(input[1].charAt(0) + "");
                int endIndex = Integer.parseInt(input[1].charAt(2) + "");
                startIndex -= 1;
                endIndex -= 1;
                int newSize = recipeList.size() - ((endIndex - startIndex) + 1);
                while (recipeList.size() != newSize) {
                    rangeRecipes.append(recipeList.deleteRecipe(startIndex).getName()).append(", ");
                }
                rangeRecipes = new StringBuilder(rangeRecipes.substring(0, rangeRecipes.length() - 2));
                return rangeRecipes.toString();
            } else {
                int recipeIndex = Integer.parseInt(input[1]);
                // need to subtract 1 since list is 1-based
                return recipeList.deleteRecipe(recipeIndex - 1).getName();
            }
        }
    }

    public String parseTagRecipe(String[] inputs, RecipeList recipeList) {
        String tag;
        boolean isOnlyTagWordInCommand = inputs.length == 1;
        boolean isAddTag;
        boolean isRemoveTag;

        if (isOnlyTagWordInCommand) {
            throw new IllegalArgumentException("Please indicate at least a tag and a recipe.");
        }

        StringBuilder commandString = new StringBuilder(inputs[1]);
        for (int i = 2; i < inputs.length; i++) {
            commandString.append(" ").append(inputs[i]);
        }

        isAddTag = commandString.indexOf(">>") == -1 && commandString.indexOf("<<") != -1;
        isRemoveTag = commandString.indexOf("<<") == -1 && commandString.indexOf(">>") != -1;

        if (!(isAddTag || isRemoveTag)) {
            throw new IllegalArgumentException("Please enter the command in the correct format.");
        } else if (isAddTag) {
            tag = parseAddRecipeTag(commandString.toString(), recipeList);
        } else if (isRemoveTag) {
            tag = parseRemoveRecipeTag(commandString.toString(), recipeList);
        } else {
            throw new IllegalArgumentException("Invalid command.");
        }
        return tag;
    }

    public String parseAddRecipeTag(String command, RecipeList recipeList) {
        String tag;
        Recipe recipe;
        String[] recipesToTag;
        boolean isUnableToFindTheRecipe;
        String[] args = command.trim().split("<<");
        boolean isNotEnoughArgs = args.length < 2 || args[0].equals("") || args[1].equals("");

        if (isNotEnoughArgs) {
            throw new IllegalArgumentException("Please enter the command in the correct format.");
        }

        tag = args[0].trim();
        recipesToTag = args[1].split("&&");
        for (String recipeName : recipesToTag) {
            recipeName = recipeName.trim();
            recipe = recipeList.findByName(recipeName);
            isUnableToFindTheRecipe = recipe == null;
            if (isUnableToFindTheRecipe) {
                String errorMessage1 = "Unable to find the recipe: \"" + recipeName + "\" in the" + " tag.";
                String errorMessage2 = "All the recipe before \"" + recipeName + "\" (if any) are "
                        + "successfully added from the tag.";
                throw new IndexOutOfBoundsException(
                        String.format("%-97s|\n| %-97s", errorMessage1, errorMessage2));
            }
            recipeList.addRecipeToTag(tag, recipe);
        }
        return tag;
    }

    public String parseRemoveRecipeTag(String command, RecipeList recipeList) {
        String tag;
        Recipe recipe;
        String[] recipesToRemove;
        boolean isUnableToFindTag;
        boolean isNoRecipeInTheList;
        String[] args = command.trim().split(">>");
        boolean isNotEnoughArgs = args.length < 2 || args[0].equals("") || args[1].equals("");

        if (isNotEnoughArgs) {
            throw new IllegalArgumentException("Please enter the command in the correct format.");
        }

        tag = args[0].trim();
        isUnableToFindTag = !recipeList.tags.containsKey(tag);
        if (isUnableToFindTag) {
            throw new IndexOutOfBoundsException(
                    "There is no \"" + tag + "\" tag found. Please make sure you have "
                            + "entered the correct tag.");
        }

        recipesToRemove = args[1].split("&&");
        for (String recipeName : recipesToRemove) {
            recipeName = recipeName.trim();
            recipe = recipeList.findByName(recipeName);
            isNoRecipeInTheList = recipe == null;
            if (isNoRecipeInTheList) {
                String errorMessage1 = "Unable to find the recipe: \"" + recipeName + "\" in the" + " tag.";
                String errorMessage2 = "All the recipe before \"" + recipeName + "\" (if any) are "
                        + "successfully removed from the tag.";
                throw new IndexOutOfBoundsException(
                        String.format("%-97s|\n| %-97s", errorMessage1, errorMessage2));
            }

            try {
                recipeList.removeRecipeFromTag(tag, recipe);
            } catch (IndexOutOfBoundsException e) {
                String errorMessage1 = "Unable to find the recipe: \"" + recipeName + "\" in the" + " tag.";
                String errorMessage2 = "All the recipe before \"" + recipeName + "\" (if any) are "
                        + "successfully removed from the tag.";
                throw new IndexOutOfBoundsException(
                        String.format("%-97s|\n| %-97s", errorMessage1, errorMessage2));
            }
        }
        return tag;
    }

    public RecipeList parseListRecipe(String[] inputs, RecipeList recipeList) {
        String[] filters;
        RecipeList recipeListToPrint;
        boolean hasTagArgs = inputs.length > 2;
        boolean isOnlyListWordInCommand = inputs.length == 1;
        boolean isTag = false;
        int firstArgsIndex = 1;

        if (isOnlyListWordInCommand) {
            filters = null;
        } else {
            isTag = inputs[1].equals("/t");
            if (isTag && !hasTagArgs) {
                throw new IllegalArgumentException("Please include at least a tag.");
            } else if (isTag) {
                firstArgsIndex = 2;
            }

            StringBuilder filterString = new StringBuilder(inputs[firstArgsIndex]);
            for (int i = firstArgsIndex + 1; i < inputs.length; i++) {
                filterString.append(" ").append(inputs[i]);
            }

            filters = filterString.toString().split("&&");
        }
        recipeListToPrint = recipeList.listRecipes(filters, isTag);
        return recipeListToPrint;
    }

    public Recipe parseViewRecipe(String[] command, RecipeList recipes) {
        assert command[0].equals("view");
        Recipe requestedRecipe;
        int recipeIndex = 0;
        try {
            recipeIndex = Integer.parseInt(command[1]);
            requestedRecipe = recipes.get(recipeIndex - 1);
        } catch (NumberFormatException error) {
            String errorMessage = String.format(
                    "Please enter a valid recipe number. You entered %s, " + "which is not a number.",
                    command[1]);
            throw new NumberFormatException(errorMessage);
        } catch (ArrayIndexOutOfBoundsException error) {
            String errorMessage = "Please enter a valid recipe number. You did not enter a recipe number.";
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        } catch (IndexOutOfBoundsException error) {
            String errorMessage = String.format(
                    "Please enter a valid recipe number. You entered %d, " + "which is out of bounds.",
                    recipeIndex);
            throw new IndexOutOfBoundsException(errorMessage);
        }
        return requestedRecipe;
    }

    public Recipe parseViewRecipe(String recipeName, RecipeList recipes) {
        int recipeIndex = 1;
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(recipeName)) {
                break;
            }
            recipeIndex++;
        }
        return recipes.get(recipeIndex - 1);
    }

    public Recipe parseRandomRecipe(RecipeList recipes) {
        if (recipes.size() == 0) {
            throw new NullPointerException("There is no recipe in the list to random");
        }
        Recipe randomRecipe = recipes.randomRecipe();
        return randomRecipe;
    }

    public WeeklyPlan parseWeeklyPlan(String[] command, RecipeList recipes)
            throws InvalidRecipeNameException, InvalidNegativeValueException {
        WeeklyPlan updatedWeeklyPlan;
        try {
            switch (command[1]) {
            case "/add":
            case "/delete":
                updatedWeeklyPlan = parseEditSingleWeeklyPlan(command, recipes);
                break;
            case "/multiadd":
            case "/multidelete":
                updatedWeeklyPlan = parseEditMultiWeeklyPlan(command, recipes);
                break;
            default:
                throw new IllegalArgumentException(
                        "Please indicate if you would want to add or delete the recipe from your weekly "
                                + "plan.");
            }

            return updatedWeeklyPlan;
        } catch (ArrayIndexOutOfBoundsException error) {
            throw new ArrayIndexOutOfBoundsException("Insufficient number of arguments provided.");
        } catch (NumberFormatException error) {
            throw new NumberFormatException("Please enter a valid number as the last argument.");
        }
    }

    private WeeklyPlan parseEditSingleWeeklyPlan(String[] command, RecipeList recipes)
            throws InvalidNegativeValueException, InvalidRecipeNameException {
        int numDays = Integer.parseInt(command[command.length - 1]);
        if (numDays < 1) {
            throw new InvalidNegativeValueException("Number of days needs to be at least 1.");
        }

        int nameLastIndex = command.length - 1;
        WeeklyPlan edits = new WeeklyPlan();
        StringBuilder recipeName = new StringBuilder(command[2]);
        for (int i = 3; i < nameLastIndex; i++) {
            recipeName.append(" ").append(command[i]);
        }

        if (recipes.findByName(recipeName.toString().trim()) != null) {
            edits.put(recipeName.toString(), numDays);
            return edits;
        } else {
            throw new InvalidRecipeNameException("Please indicate a valid recipe name.");
        }
    }

    private WeeklyPlan parseEditMultiWeeklyPlan(String[] command, RecipeList recipes)
            throws InvalidNegativeValueException, InvalidRecipeNameException {
        WeeklyPlan recipesToEdit = new WeeklyPlan();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<String> recipeNames = new ArrayList<>();
        ArrayList<Integer> startIndices = new ArrayList<>();
        ArrayList<Integer> endIndices = new ArrayList<>();
        StringBuilder recipeName = new StringBuilder();

        for (int i = 0; i < command.length; i++) {
            if (command[i].equals("/r")) {
                startIndices.add(i);
            } else if (command[i].equals("/q")) {
                endIndices.add(i);
                int quantity = Integer.parseInt(command[i + 1]);
                if (quantity < 1) {
                    throw new InvalidNegativeValueException(
                            "Please enter a positive number for the quantity.");
                }
                quantities.add(quantity);
            }
        }

        // Checks that command is entered in the correct
        if (startIndices.size() != endIndices.size()) {
            throw new IllegalArgumentException("Please ensure the number of recipes and quantities match.");
        }

        if (startIndices.size() == 0) {
            throw new IllegalArgumentException(
                    "Please ensure that the command is entered in the correct format.");
        }

        // Building the recipe names
        for (int i = 0; i < startIndices.size(); i++) {
            int nameStartIndex = startIndices.get(i) + 1;
            int nameEndIndex = endIndices.get(i) - 1;
            recipeName = getRecipeNames(command, recipeNames, recipeName, nameStartIndex, nameEndIndex);
        }

        // Add each recipe to the weekly plan
        try {
            for (int i = 0; i < recipeNames.size(); i++) {
                if (recipes.findByName(recipeNames.get(i)) != null) {
                    recipesToEdit.put(recipeNames.get(i), quantities.get(i));
                } else {
                    throw new InvalidRecipeNameException("Please indicate a valid recipe name.");
                }
            }
        } catch (NumberFormatException error) {
            throw new NumberFormatException("Please enter a positive number for the quantity.");
        }

        return recipesToEdit;
    }

    private StringBuilder getRecipeNames(String[] command, ArrayList<String> recipeNames,
            StringBuilder recipeName, int nameStartIndex, int nameEndIndex) {
        recipeName.append(command[nameStartIndex].toLowerCase().trim());
        for (int j = nameStartIndex + 1; j <= nameEndIndex; j++) {
            recipeName.append(" ").append(command[j].toLowerCase().trim());
        }
        recipeNames.add(recipeName.toString());
        recipeName = new StringBuilder();
        return recipeName;
    }

    // parser to read dd/mm/yyyy format as local date catching invalid date format
    public LocalDate parseDate(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Please enter a valid date in the format dd/mm/yyyy");
        }
    }

    public void parseAddUserIngredients(String[] command, IngredientList ingredientList) {
        String ingredientName = null;
        Integer ingredientCount = null;
        String expiryDate = null;

        try {
            for (int i = 1; i < command.length; i++) {
                switch (command[i]) {
                case "/n":
                    ingredientName = command[++i];
                    break;
                case "/c":
                    ingredientCount = Integer.parseInt(command[++i]);
                    break;
                case "/d":
                    expiryDate = command[++i];
                    break;
                default:
                    throw new IllegalArgumentException("Missing required information. Please provide "
                            + "ingredient name, count, and expiry date.");
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("Invalid command format. Please check your input.");
        }

        if (ingredientName == null || ingredientCount == null || expiryDate == null) {
            throw new IllegalArgumentException("Missing required information. Please use /n, /c, and /d.");
        }

        ingredientList.addIngredient(new Ingredient(ingredientName, ingredientCount, expiryDate));
    }

    public void parseDeleteUserIngredients(String[] command, IngredientList userIngredients) {
        String ingredientName = null;
        Integer ingredientCount = null;

        try {
            for (int i = 1; i < command.length; i++) {
                switch (command[i]) {
                case "/n":
                    ingredientName = command[++i];
                    break;
                case "/c":
                    ingredientCount = Integer.parseInt(command[++i]);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Missing required information. Please provide " + "ingredient name and count.");
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("Invalid command format. Please check your input.");
        }

        if (ingredientName == null || ingredientCount == null) {
            throw new IllegalArgumentException("Missing required information. Please use /n and /c.");
        }

        userIngredients.deleteIngredient(ingredientName, ingredientCount);
    }
}
