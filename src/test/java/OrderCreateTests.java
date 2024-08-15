import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Ingredients;
import order.Order;
import order.OrderSteps;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserSteps;

import java.util.ArrayList;

public class OrderCreateTests {
    private String accessToken;


    @Test
    @DisplayName("Cоздать заказ без авторизации")
    public void createOrderWithoutAuthTest() {
        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        Response response = OrderSteps.createOrderWithoutAuth(order);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Создать заказ с авторизацией")
    public void createOrderWithAuthTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Ошибка создания заказа с авторизацией и без ингредиентов")
    public void failCreateOrderWithAuthWithoutIngredientsTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Order order = new Order(null);
        response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Ошибка создания заказа без авторизации и без ингредиентов")
    public void failCreateOrderWithoutAuthAndIngredientsTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Order order = new Order(null);
        response = OrderSteps.createOrderWithoutAuth(order);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Ошибка создания заказа с авторизацией и c невалидным ингредиентом")
    public void failCreateOrderWithAuthAndIncorrectIngredientsTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        Order order = new Order(ingredient1);
        response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }
    @Test
    @DisplayName("Ошибка создания заказа с авторизацией и c одним невалидным ингредиентом")
    public void failCreateOrderWithAuthAndOneIncorrectIngredientTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }}
