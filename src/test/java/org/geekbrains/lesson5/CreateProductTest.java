package org.geekbrains.lesson5;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.geekbrains.lessson5.api.ProductService;
import org.geekbrains.lessson5.dto.Product;
import org.geekbrains.lessson5.util.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateProductTest {

    static ProductService productService;
    Product product = null;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();
        assertThat(createProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(createProductResponse.body().getId(), notNullValue());

        id =  createProductResponse.body().getId();

        Response<Product> getProductResponse = productService.getProductById(id)
                .execute();
        assertThat(getProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(getProductResponse.body().getId(), equalTo(id));
        assertThat(getProductResponse.body().getTitle(), equalTo(product.getTitle()));
        assertThat(getProductResponse.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(getProductResponse.body().getPrice(), equalTo(product.getPrice()));

        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    void createProductWithExtraFieldTest() throws IOException {
        product.setId(2);

        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();

        assertThat(createProductResponse.code(), equalTo(400));
        assertThat(createProductResponse.errorBody().string(), containsString("Id must be null for new entity"));
    }
}
