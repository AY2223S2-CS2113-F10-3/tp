# User Guide

## Introduction

Meal360 is a desktop app for managing your recipes, optimized for use via a Command Line Interface (
CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast,
Meal360 can get your recipe management tasks done faster than traditional GUI apps.

___

## Quick Start

1. Ensure that you have Java 11 or above installed.
2. Down the latest version of `Duke` from [here](http://link.to/duke).

___

## Features

* [View recipes](#viewing-recipes--view)
* [List relevant recipes](#listing-recipes--list)
* [Tag/Categorise recipes](#taggingcategorising-recipes--tag)
* [Add single recipe to weekly plan](#adding-to-this-weeks-plan--weekly-add)
* [Add multiple recipe to weekly plan](#adding-to-this-weeks-plan--weekly-add)
* [Remove single recipe from weekly plan](#deleting-from-this-weeks-plan--weekly-delete)
* [Remove multiple recipe from weekly plan](#deleting-from-this-weeks-plan--weekly-delete)
* [View weekly plan](#view-this-weeks-plan--weeklyplan)
* [Random a recipe](#random-a-recipe--random)

### Adding recipes: `add`

Add new recipes to your list.

Format: `add /r [recipe_name]`

* `recipe_name` is basically a string.
* Type the recipe name after typing `/r `.

Example of usage:

`add /r chicken rice`
`add /r noodles`

### Editing recipes: `edit`

Edit recipes in your list either partially or fully.

Format: `edit /r [recipe_name]`

* `recipe_name` is basically a string.
* Type the recipe name after typing `/r `.

Example of usage:

`edit /r chicken rice`
`edit /r noodles`

### Listing recipes: `list`

List all recipes or filtered list recipes by the name or ingredients.

Format: `list [/t] [KEYWORD && KEYWORD && ...]`

* The `KEYWORD` and `/t` are optional.
* `KEYWORD` can be a name of recipe or ingredients or tag.
* If user want to list recipes from tags `/t` is required. When `/t` is used,
  at least one `KEYWORD`, replacing by the tag name, is required.
* To list all recipes, do not add `KEYWORD` and `/t`.
* Use `&&` to list all the recipe that contain **all** specified `KEYWORD`.

Example of usage:

* `list` lists all the recipes.
* `list pizza` lists recipes that contain 'pizza' in the name or ingredients.
* `list milk && egg` lists recipes that contain __both__ 'milk' and 'egg' in the
  name or ingredients.
* `list /t western` lists the recipes that are in 'western' tag.
* `list /t western && chinese` lists recipes that is under __both__ 'western' and
  'chinese' tag.

### Viewing recipes: `view`

Views the list of ingredients and their quantities for a recipe.

Format: `view INDEX`

* Views the recipe at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1,2,3, ...

Example of usage:

`view 1`

### Deleting recipes: `delete`

Deletes one, a range, or all recipes currently in list.

Format: `delete INDEX/RANGE` or `delete r/NAME`

* Deletes the recipe(s) specified by the user either through the recipe index or name.
* The index refers to the index number shown in the displayed person list.
* The name refers to the recipe name in the list.

Example of usage:

`delete 1`
`delete 1-3`
`delete r/pizza`
`delete r/all`

### Tagging/Categorising recipes: `tag`

__Categorize recipes into a specific tag__

Adding one or more recipes into a tag.

Format: `tag LABEL << [RECIPE_NAME && RECIPE_NAME && ...]`

* `LABEL` is required, and replace with the tag label that user want to
  add the recipes into. This is case-sensitive.
* At least one `RECIPE_NAME` is required.
* To add multiple recipes into the tag, use `&&` followed by next `RECIPE_NAME`.

Example of usage:

* `tag western << burger` adds burger into 'western' tag.
* `tag junk foods << burger && hotdog` adds burger and hotdog into 'junk foods' tag.
* `tag breakfast << milk && boiled egg && bread` adds milk, boiled egg and
  bread into 'breakfast' tag.

__Removing recipes form a tag__

Remove recipes from a specific tag.

Format: `tag LABEL >> [RECIPE_NAME && RECIPE_NAME && ...]`

* `LABEL` is required, and replace with the tag label that user want to
  remove the recipes from. This is case-sensitive.
* At least one `RECIPE_NAME` is required.
* To remove multiple recipes from the tag, use `&&` followed by next `RECIPE_NAME`.

Example of usage:

* `tag western>> burger` removes burger from 'western' tag.
* `tag junk foods >> burger && hotdog` remove burger and hotdog from 'junk foods' tag.
* `tag breakfast >> milk && boilded egg && bread` removes milk, boiled egg, and bread
  from 'breakfast' tag.

### Adding single recipe this week's plan: `weekly /add`

Adds an existing recipe to this week's plan.

Format: `weekly /add RECIPE NAME QUANTITY`

* Adds the specified `RECIPE NAME` to this week's plan`QUANTITY` number of times.
* The recipe name refers to the name of the recipe shown in the displayed recipe list.
* The quantity **must be a positive
  integer** 1,2,3, ...

Example of usage:

* `weekly /add pizza 2` adds pizza to this week's plan twice.
* `weekly /add burger 1` adds burger to this week's plan once.

### Adding multiple recipes to this week's plan: `weekly /multiadd`

Adds multiple existing recipe to this week's plan.

Format: `weekly /multiadd [/r RECIPE NAME /q QUANTITY]`

* Adds the specified `RECIPE NAME` to this week's plan`QUANTITY` number of times.
* At least one pair of `RECIPE_NAME` and `QUANTITY` is required.
* Each `RECIPE_NAME` and `QUANTITY` requires `/r` and `/q` before it respectively.
* The recipe name refers to the name of the recipe shown in the displayed recipe list.
* The quantity **must be a positive
  integer** 1,2,3, ...

Example of usage:

* `weekly /multiadd /r pizza /q 2 /r burger /q 9` adds pizza twice and burger once to this week's
  plan.

### Deleting from this week's plan: `weekly /delete`

Deletes an existing recipe from this week's plan.

Format: `weekly /delete RECIPE NAME`

* Deletes the specified `RECIPE NAME` to this week's plan.
* The recipe name refers to the name of the recipe shown in the displayed recipe list.

Example of usage:

* `weekly /delete pizza` removes pizza from this week's plan.

### Deleting multiple recipes from this week's plan: `weekly /multidelete`

Deletes multiple existing recipe from this week's plan.

Format: `weekly /multidelete [/r RECIPE NAME /q QUANTITY]`

* Deletes the specified `RECIPE NAME` from this week's plan`QUANTITY` number of times.
* At least one pair of `RECIPE_NAME` and `QUANTITY` is required.
* Each `RECIPE_NAME` and `QUANTITY` requires `/r` and `/q` before it respectively.
* The recipe name refers to the name of the recipe shown in the displayed recipe list.
* The quantity **must be a positive
  integer** 1,2,3, ...

Example of usage:

* `weekly /multidelete /r pizza /q 2 /r burger /q 9` deletes pizza twice and burger once from this
  week's
  plan.

### Clearing this week's plan: `weekly /clear`

Clears this week's plan by removing all recipes listed in weekly plan.

Format: `weekly /clear`

### View this week's plan: `weeklyplan`

View this week's plan.

Format: `weeklyplan`

### View this week's ingredients: `weeklyingredients`

View this week's ingredients.

Format: `weeklyingredients`

### Random a recipe: `random`

Random a recipe from all the recipes that user have, and show the list of
ingredients and their quantities for a recipe.

Format: `random`

### Exit the program: `bye`

Exits the program.

Format: `bye`

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: {your answer here}

## Command Summary

| Action                             | Format, Examples                                                                                            | 
|------------------------------------|-------------------------------------------------------------------------------------------------------------|
| Add recipe                         | `add /r [recipe_name]`<br/>e.g `add /r chicken rice`                                                        |
| Edit recipe                        | `edit /r [recipe_name]`<br/>e.g `edit /r chicken rice`                                                      |
| List recipe                        | `list [/t] [KEYWORD]`<br/>e.g `list pizza`                                                                  |
| View recipe                        | `view INDEX`<br/>e.g `view 1`                                                                               |
| Add tag/Categorise to recipes      | `tag LABEL << RECIPE_NAME`<br/>e.g `tag western << pizza`                                                   |
| Remove tag/Categorise from recipes | `tag LABEL >> RECIPE_NAME`<br/>e.g `tag western >> pizza`                                                   |
| Add to weekly plan                 | `weekly /add RECIPE NAME QUANTITY`<br/>e.g `weekly /add pizza 2`                                            |
| Add multiple to weekly plan        | `weekly /multiadd [/r RECIPE NAME /q QUANTITY]` <br/>e.g `weekly /multiadd /r pizza /q 1 /r burger /q 3`    |
| Delete from weekly plan            | `weekly /delete RECIPE NAME`<br/>e.g `weekly /delete pizza`                                                 |
| Delete multiple from weekly plan   | `weekly /multidelete [/r RECIPE NAME /q QUANTITY]` <br/>e.g `weekly /multiadd /r pizza /q 1 /r burger /q 4` |
| View weekly plan                   | `weeklyplan`                                                                                                |
