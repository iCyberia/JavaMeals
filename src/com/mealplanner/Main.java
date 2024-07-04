package com.mealplanner;

import com.mealplanner.models.Ingredient;
import com.mealplanner.models.Meal;
import com.mealplanner.services.GroceryListGenerator;
import com.mealplanner.services.MealFileManager;
import com.mealplanner.services.MealPlanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final MealPlanner mealPlanner = new MealPlanner();
    private static final GroceryListGenerator groceryListGenerator = new GroceryListGenerator();
    private static final List<Meal> selectedMealsForWeek = new ArrayList<>();

    public static void main(String[] args) {
        mealPlanner.setMeals(MealFileManager.loadMeals());
        while (true) {
            System.out.println("\nMeal Planner Menu");
            System.out.println("1. Add Meal");
            System.out.println("2. Add Ingredients to Meal");
            System.out.println("3. View Meals");
            System.out.println("4. Select Meals for the Week");
            System.out.println("5. Generate Grocery List");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addMeal();
                    break;
                case 2:
                    addIngredientsToMeal();
                    break;
                case 3:
                    viewMeals();
                    break;
                case 4:
                    selectMealsForWeek();
                    break;
                case 5:
                    generateGroceryList();
                    break;
                case 6:
                    MealFileManager.saveMeals(mealPlanner.getMeals());
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addMeal() {
        System.out.print("Enter meal name: ");
        String mealName = scanner.nextLine();
        Meal meal = new Meal(mealName);
        mealPlanner.addMeal(meal);
        System.out.println("Meal added: " + mealName);
    }

    private static void addIngredientsToMeal() {
        System.out.println("Meals:");
        List<Meal> meals = mealPlanner.getMeals();
        for (int i = 0; i < meals.size(); i++) {
            System.out.println((i + 1) + ". " + meals.get(i).getName());
        }
        System.out.print("Select a meal to add ingredients to (by number): ");
        int mealIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume newline

        if (mealIndex >= 0 && mealIndex < meals.size()) {
            Meal meal = meals.get(mealIndex);
            while (true) {
                System.out.print("Enter ingredient name (or type 'done' to finish): ");
                String ingredientName = scanner.nextLine();
                if (ingredientName.equalsIgnoreCase("done")) {
                    break;
                }
                System.out.print("Enter quantity: ");
                double quantity = scanner.nextDouble();
                scanner.nextLine();  // Consume newline
                System.out.print("Enter unit: ");
                String unit = scanner.nextLine();
                meal.addIngredient(new Ingredient(ingredientName, quantity, unit));
            }
            System.out.println("Ingredients added to " + meal.getName());
        } else {
            System.out.println("Invalid meal selection.");
        }
    }

    private static void viewMeals() {
        List<Meal> meals = mealPlanner.getMeals();
        if (meals.isEmpty()) {
            System.out.println("No meals available.");
        } else {
            for (Meal meal : meals) {
                System.out.println(meal);
            }
        }
    }

    private static void selectMealsForWeek() {
        selectedMealsForWeek.clear();
        while (true) {
            System.out.println("Meals:");
            List<Meal> meals = mealPlanner.getMeals();
            for (int i = 0; i < meals.size(); i++) {
                System.out.println((i + 1) + ". " + meals.get(i).getName());
            }
            System.out.print("Select a meal to add to the weekly plan (by number, or type 'done' to finish): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            try {
                int mealIndex = Integer.parseInt(input) - 1;
                if (mealIndex >= 0 && mealIndex < meals.size()) {
                    selectedMealsForWeek.add(meals.get(mealIndex));
                    System.out.println("Added: " + meals.get(mealIndex).getName());
                } else {
                    System.out.println("Invalid meal selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'done'.");
            }
        }
    }

    private static void generateGroceryList() {
        if (selectedMealsForWeek.isEmpty()) {
            System.out.println("No meals selected for the week.");
            return;
        }

        List<Ingredient> groceryList = groceryListGenerator.generateGroceryList(selectedMealsForWeek);
        if (groceryList.isEmpty()) {
            System.out.println("No meals selected.");
        } else {
            System.out.println("Grocery List:");
            for (Ingredient ingredient : groceryList) {
                System.out.println(ingredient);
            }
        }
    }
}
