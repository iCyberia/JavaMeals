package com.mealplanner.models;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private final String name;
    private final List<Ingredient> ingredients;

    public Meal(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return name + ": " + ingredients;
    }
}
