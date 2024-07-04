package com.mealplanner.services;

import com.mealplanner.models.Ingredient;
import com.mealplanner.models.Meal;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MealFileManager {
    private static final String FILE_NAME = "meals.txt";

    public static void saveMeals(List<Meal> meals) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Meal meal : meals) {
                writer.write(meal.getName() + "\n");
                for (Ingredient ingredient : meal.getIngredients()) {
                    writer.write(ingredient.getName() + "," + ingredient.getQuantity() + "," + ingredient.getUnit() + "\n");
                }
                writer.write("---\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Meal> loadMeals() {
        List<Meal> meals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            Meal meal = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    meals.add(meal);
                    meal = null;
                } else if (meal == null) {
                    meal = new Meal(line);
                } else {
                    String[] parts = line.split(",");
                    Ingredient ingredient = new Ingredient(parts[0], Double.parseDouble(parts[1]), parts[2]);
                    meal.addIngredient(ingredient);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return meals;
    }
}

