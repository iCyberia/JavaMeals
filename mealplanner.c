#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_INGREDIENTS 100
#define MAX_MEALS 100
#define NAME_LEN 50
#define UNIT_LEN 20

typedef struct {
    char name[NAME_LEN];
    double quantity;
    char unit[UNIT_LEN];
} Ingredient;

typedef struct {
    char name[NAME_LEN];
    Ingredient ingredients[MAX_INGREDIENTS];
    int ingredient_count;
} Meal;

Meal meals[MAX_MEALS];
int meal_count = 0;

Ingredient grocery_list[MAX_INGREDIENTS * MAX_MEALS];
int grocery_list_count = 0;

void add_meal() {
    if (meal_count >= MAX_MEALS) {
        printf("Maximum number of meals reached.\n");
        return;
    }

    printf("Enter meal name: ");
    fgets(meals[meal_count].name, NAME_LEN, stdin);
    meals[meal_count].name[strcspn(meals[meal_count].name, "\n")] = '\0';  // Remove newline character
    meals[meal_count].ingredient_count = 0;
    meal_count++;
    printf("Meal added.\n");
}

void add_ingredient_to_meal() {
    int meal_index;
    printf("Select a meal to add ingredients to (by number):\n");
    for (int i = 0; i < meal_count; i++) {
        printf("%d. %s\n", i + 1, meals[i].name);
    }
    scanf("%d", &meal_index);
    getchar();  // Consume newline character

    if (meal_index < 1 || meal_index > meal_count) {
        printf("Invalid meal selection.\n");
        return;
    }
    meal_index--;  // Adjust for zero-based index

    if (meals[meal_index].ingredient_count >= MAX_INGREDIENTS) {
        printf("Maximum number of ingredients for this meal reached.\n");
        return;
    }

    Ingredient *ingredient = &meals[meal_index].ingredients[meals[meal_index].ingredient_count];
    printf("Enter ingredient name: ");
    fgets(ingredient->name, NAME_LEN, stdin);
    ingredient->name[strcspn(ingredient->name, "\n")] = '\0';  // Remove newline character

    printf("Enter quantity: ");
    scanf("%lf", &ingredient->quantity);
    getchar();  // Consume newline character

    printf("Enter unit: ");
    fgets(ingredient->unit, UNIT_LEN, stdin);
    ingredient->unit[strcspn(ingredient->unit, "\n")] = '\0';  // Remove newline character

    meals[meal_index].ingredient_count++;
    printf("Ingredient added to meal %s.\n", meals[meal_index].name);
}

void view_meals() {
    if (meal_count == 0) {
        printf("No meals available.\n");
        return;
    }
    for (int i = 0; i < meal_count; i++) {
        printf("Meal: %s\n", meals[i].name);
        for (int j = 0; j < meals[i].ingredient_count; j++) {
            Ingredient *ingredient = &meals[i].ingredients[j];
            printf("  - %s: %.2f %s\n", ingredient->name, ingredient->quantity, ingredient->unit);
        }
    }
}

void save_meals_to_file(const char *filename) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Failed to open file for writing");
        return;
    }

    for (int i = 0; i < meal_count; i++) {
        fprintf(file, "%s\n", meals[i].name);
        for (int j = 0; j < meals[i].ingredient_count; j++) {
            Ingredient *ingredient = &meals[i].ingredients[j];
            fprintf(file, "%s,%.2f,%s\n", ingredient->name, ingredient->quantity, ingredient->unit);
        }
        fprintf(file, "---\n");
    }

    fclose(file);
    printf("Meals saved to file.\n");
}

void load_meals_from_file(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Failed to open file for reading");
        return;
    }

    meal_count = 0;
    while (!feof(file)) {
        if (meal_count >= MAX_MEALS) {
            printf("Maximum number of meals reached.\n");
            break;
        }

        Meal *meal = &meals[meal_count];
        if (fgets(meal->name, NAME_LEN, file) == NULL) break;
        meal->name[strcspn(meal->name, "\n")] = '\0';  // Remove newline character
        meal->ingredient_count = 0;

        while (!feof(file)) {
            char line[NAME_LEN + UNIT_LEN + 20];  // Buffer for reading lines
            if (fgets(line, sizeof(line), file) == NULL) break;
            if (strcmp(line, "---\n") == 0) break;

            Ingredient *ingredient = &meal->ingredients[meal->ingredient_count];
            sscanf(line, "%[^,],%lf,%[^\n]", ingredient->name, &ingredient->quantity, ingredient->unit);
            meal->ingredient_count++;
        }
        meal_count++;
    }

    fclose(file);
    printf("Meals loaded from file.\n");
}

void generate_grocery_list() {
    grocery_list_count = 0;

    for (int i = 0; i < meal_count; i++) {
        Meal *meal = &meals[i];
        for (int j = 0; j < meal->ingredient_count; j++) {
            Ingredient *ingredient = &meal->ingredients[j];
            int found = 0;
            for (int k = 0; k < grocery_list_count; k++) {
                if (strcmp(grocery_list[k].name, ingredient->name) == 0 &&
                    strcmp(grocery_list[k].unit, ingredient->unit) == 0) {
                    grocery_list[k].quantity += ingredient->quantity;
                    found = 1;
                    break;
                }
            }
            if (!found) {
                grocery_list[grocery_list_count] = *ingredient;
                grocery_list_count++;
            }
        }
    }
}

void view_grocery_list() {
    if (grocery_list_count == 0) {
        printf("Grocery list is empty.\n");
        return;
    }

    printf("Grocery List:\n");
    for (int i = 0; i < grocery_list_count; i++) {
        Ingredient *ingredient = &grocery_list[i];
        printf("  - %s: %.2f %s\n", ingredient->name, ingredient->quantity, ingredient->unit);
    }
}

void save_grocery_list_to_file(const char *filename) {
    FILE *file = fopen(filename, "w");
    if (!file) {
        perror("Failed to open file for writing");
        return;
    }

    for (int i = 0; i < grocery_list_count; i++) {
        Ingredient *ingredient = &grocery_list[i];
        fprintf(file, "%s,%.2f,%s\n", ingredient->name, ingredient->quantity, ingredient->unit);
    }

    fclose(file);
    printf("Grocery list saved to file.\n");
}

void print_menu() {
    printf("\nMeal Planner Menu\n");
    printf("1. Add Meal\n");
    printf("2. Add Ingredients to Meal\n");
    printf("3. View Meals\n");
    printf("4. Generate Grocery List\n");
    printf("5. View Grocery List\n");
    printf("6. Save Meals to File\n");
    printf("7. Load Meals from File\n");
    printf("8. Save Grocery List to File\n");
    printf("9. Exit\n");
    printf("Choose an option: ");
}

int main() {
    const char *meal_filename = "meals.txt";
    const char *grocery_filename = "grocery_list.txt";
    load_meals_from_file(meal_filename);

    while (1) {
        print_menu();
        int choice;
        scanf("%d", &choice);
        getchar();  // Consume newline character

        switch (choice) {
            case 1:
                add_meal();
                break;
            case 2:
                add_ingredient_to_meal();
                break;
            case 3:
                view_meals();
                break;
            case 4:
                generate_grocery_list();
                break;
            case 5:
                view_grocery_list();
                break;
            case 6:
                save_meals_to_file(meal_filename);
                break;
            case 7:
                load_meals_from_file(meal_filename);
                break;
            case 8:
                save_grocery_list_to_file(grocery_filename);
                break;
            case 9:
                printf("Exiting...\n");
                exit(0);
            default:
                printf("Invalid choice. Try again.\n");
        }
    }

    return 0;
}
