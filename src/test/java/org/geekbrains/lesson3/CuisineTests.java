package org.geekbrains.lesson3;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.geekbrains.lesson3.Utils.getConfigValue;
import static org.hamcrest.Matchers.equalTo;

public class CuisineTests {

    @Test
    void classifyCuisineWithoutQueryParametersTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Mediterranean"))
                .assertThat().body("confidence", equalTo(0.0F))
                .statusCode(200);
    }

    @Test
    void classifyCuisineWithWrongRequestTypeTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .get("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .statusCode(405);
    }

    @Test
    void classifyCuisineWithAmericanTypeTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Burger")
                .param("ingredientList", "3 oz pork shoulder")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("American"))
                .assertThat().body("confidence", equalTo(0.85F))
                .statusCode(200);
    }

    @Test
    void classifyCuisineWithVietnameseTypeTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Pho")
                .param("ingredientList", "Tofu")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Vietnamese"))
                .assertThat().body("confidence", equalTo(0.85F))
                .statusCode(200);
    }

    @Test
    void classifyCuisineWithEmptyIngredientListTest() {
        given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Falafel Burger")
                .param("ingredientList", "")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Middle Eastern"))
                .assertThat().body("confidence", equalTo(0.85F))
                .statusCode(200);
    }
}
