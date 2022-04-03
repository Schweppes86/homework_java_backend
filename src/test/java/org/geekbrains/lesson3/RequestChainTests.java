package org.geekbrains.lesson3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.geekbrains.lesson3.Utils.getConfigValue;
import static org.hamcrest.Matchers.equalTo;

public class RequestChainTests {

    private String id;
    private String user = "lesnikov" + new Random().nextInt(9999);
    private String email = "mail" + new Random().nextInt(9999) + "@example.ru";
    private String hash;

    @BeforeEach
    void createUserTest() {
        hash = given()
                .queryParam("apiKey", getConfigValue("apiKey"))
                .body("{\n"
                        + " \"username\": \""+ user +"\",\n"
                        + " \"firstName\": \"first name\",\n"
                        + " \"lastName\": \"last name\",\n"
                        + " \"email\": \""+ email +"\"\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/users/connect")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("hash")
                .toString();
    }


    @Test
    void addMealWithIngredientTypeTest() {
        id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getConfigValue("apiKey"))
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/"+ user +"/items")
                .then()
                .statusCode(200)
                .assertThat().body("status", equalTo("success"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    void addMealWithRecipeTypeTest() {
        id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getConfigValue("apiKey"))
                .body("{\n"
                        + " \"date\": 1589500800,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"RECIPE\",\n"
                        + " \"value\": {\n"
                        + " \"id\": 296213,\n"
                        + " \"servings\": 2,\n"
                        + " \"title\": \"Spinach Salad with Roasted Vegetables and Spiced Chickpea\",\n"
                        + " \"imageType\": \"jpg\"\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/"+ user +"/items")
                .then()
                .statusCode(200)
                .assertThat().body("status", equalTo("success"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        given()
                .queryParam("hash", hash)
                .queryParam("apiKey", getConfigValue("apiKey"))
                .delete("https://api.spoonacular.com/mealplanner/"+ user+"/items/" + id)
                .then()
                .statusCode(200);
    }
}
