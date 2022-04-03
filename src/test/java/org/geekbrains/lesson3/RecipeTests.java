package org.geekbrains.lesson3;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.geekbrains.lesson3.Utils.getConfigValue;
import static org.hamcrest.Matchers.*;

public class RecipeTests {

    //1. Автоматизируйте GET /recepies/complexSearch (минимум 5 кейсов) и POST /recipes/cuisine (минимум 5 кейсов), используя rest-assured.
    //2. Сделать автоматизацию цепочки (хотя бы 1 тест со всеми эндпоинтами) для создания и удаления блюда в MealPlan).
    // Подумайте, как использовать tearDown при тестировании POST /mealplanner/:username/shopping-list/items
    //3. Воспользуйтесь кейсами, которые вы написали в ДЗ №2, перенеся всю логику из постман-коллекции в код.
    //4. Сдайте ссылку на репозиторий, указав ветку с кодом.

    @Test
    void getRecipeWithoutQueryParametersTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .prettyPeek()
                .then()
                .assertThat().body("totalResults", equalTo(5222))
                .statusCode(200);
    }

    @Test
    void getRecipeWithWrongRequestTypeTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .when()
                .post("https://api.spoonacular.com/recipes/complexSearch")
                .prettyPeek()
                .then()
                .statusCode(405);
    }

    @Test
    void getRecipeWithQueryParameterTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .queryParam("query", "burger")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .prettyPeek()
                .then()
                .assertThat().body("totalResults", equalTo(54))
                .statusCode(200);
    }

    @Test
    void getRecipeWithQueryAndDietParametersTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .prettyPeek()
                .then()
                .assertThat().body("totalResults", equalTo(3))
                .assertThat().body("results[0].title", containsString("Falafel Burger"))
                .statusCode(200);
    }

    @Test
    void getRecipeWithExcludeCuisineParameterTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .queryParam("excludeCuisine", "greek")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .prettyPeek()
                .then()
                .assertThat().body("totalResults", equalTo(5198))
                .statusCode(200);
    }
}
