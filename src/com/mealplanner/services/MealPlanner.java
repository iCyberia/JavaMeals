package com.mealplanner.services;

import com.mealplanner.models.Meal;
import java.util.ArrayList;
import java.util.List;

public class MealPlanner {
    private List<Meal> meals;

    public MealPlanner() {
        meals = new ArrayList<>();
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
