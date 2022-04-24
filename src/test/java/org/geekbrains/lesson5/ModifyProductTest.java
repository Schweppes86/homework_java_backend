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
import static org.hamcrest.Matchers.equalTo;

public class ModifyProductTest {
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
    void updateProductInFoodCategoryTest() throws IOException {
        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();
        assertThat(createProductResponse.isSuccessful(), CoreMatchers.is(true));
        id = createProductResponse.body().getId();

        Product newProduct = new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        Response<Product> updateProductResponse = productService.modifyProduct(newProduct)
                .execute();
        assertThat(updateProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(updateProductResponse.body().getId(), equalTo(id));
        assertThat(updateProductResponse.body().getTitle(), equalTo(newProduct.getTitle()));
        assertThat(updateProductResponse.body().getCategoryTitle(), equalTo(newProduct.getCategoryTitle()));
        assertThat(updateProductResponse.body().getPrice(), equalTo(newProduct.getPrice()));

        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    void updateUnknownIdProductTest() throws IOException {
        product.setId(252);
        Response<Product> updateProductResponse = productService.modifyProduct(product)
                .execute();
        assertThat(updateProductResponse.code(), equalTo(400));
    }
}
