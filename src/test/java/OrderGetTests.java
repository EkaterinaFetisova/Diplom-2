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


public class OrderGetTests {
    private String accessToken;
    @Test
    @DisplayName("Получение заказа с авторизацией")
    public void getOrderWithAuthTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        OrderSteps.createOrderWithAuth(order, accessToken);

        response = OrderSteps.getOrders(accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Ошибка получения заказа без авторизации")
    public void failGetOrderWithoutAuthTest() {

        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderSteps.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        OrderSteps.createOrderWithAuth(order, accessToken);

        response = OrderSteps.getOrders();
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }}
