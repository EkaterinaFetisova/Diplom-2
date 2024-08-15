package user;

import data.UserRandomizer;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static data.ApiPaths.*;

public class UserSteps {
    public static User createNewUser() {
        return new User(UserRandomizer.getRandomUser().getEmail(), UserRandomizer.getRandomUser().getPassword(), UserRandomizer.getRandomUser().getName());
    }
    @Step("Создать нового пользователя")
    public static Response createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_REGISTER_PATH);
    }
    @Step("Авторизовать пользователя")
    public static Response loginUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_AUTH_PATH);
    }

    @Step("Обновить пользователя с авторизацией")
    public static Response updateUserWithAuth(User user, String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .body(user)
                .when()
                .patch(USER_PATH);
    }
    @Step("Обновить пользователя без авторизации")
    public static Response updateUserWithoutAuth(User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER_PATH);
    }

    @Step("Удалить пользователя")
    public static void deleteUser(String accessToken) {
                 spec()
                .header("Authorization",accessToken)
                .when()
                .delete(USER_PATH);
    }
}
