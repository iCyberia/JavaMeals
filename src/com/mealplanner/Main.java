package com.mealplanner;

import com.mealplanner.models.*;
import com.mealplanner.services.*;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MealPlanner mealPlanner = new MealPlanner();
    private static GroceryListGenerator groceryListGenerator = new GroceryListGenerator();

    public static void main(String[] args) {
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
        // Placeholder for selecting meals for the week (e.g., by index)
        // For simplicity, assume all meals are selected for now
    }

    private static void generateGroceryList() {
        List<Meal> selectedMeals = mealPlanner.getMeals();  // Placeholder for selected meals
        List<Ingredient> groceryList = groceryListGenerator.generateGroceryList(selectedMeals);
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
