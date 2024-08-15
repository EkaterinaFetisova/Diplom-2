package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static data.ApiPaths.*;

public class OrderSteps {
    @Step("Получить ингредиенты")
    public static Ingredients getIngredient() {
        return spec()
                .when()
                .get(INGREDIENTS_PATH)
                .body()
                .as(Ingredients.class);
    }
    @Step("Создать заказ без авторизации")
    public static Response createOrderWithoutAuth(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_PATH);
    }
    @Step("Создать заказ с авторизацией")
    public static Response createOrderWithAuth(Order order, String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }
    @Step("Получить заказа с авторизацией")
    public static Response getOrders(String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .when()
                .get(ORDER_PATH);
    }
    @Step("Получить заказа без авторизации")
    public static Response getOrders() {
        return spec()
                .when()
                .get(ORDER_PATH);
    }
}
