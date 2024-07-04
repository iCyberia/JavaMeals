package com.mealplanner.services;

import com.mealplanner.models.Meal;
import com.mealplanner.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class GroceryListGenerator {

    public List<Ingredient> generateGroceryList(List<Meal> selectedMeals) {
        List<Ingredient> groceryList = new ArrayList<>();
        for (Meal meal : selectedMeals) {
            groceryList.addAll(meal.getIngredients());
        }
        return groceryList;
    }
}
