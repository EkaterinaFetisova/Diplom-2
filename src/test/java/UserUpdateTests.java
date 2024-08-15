import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserSteps;


public class UserUpdateTests {
    String accessToken;
    @Test
    @DisplayName("Обновить имя и почту пользователя")
    public void updateUserTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserSteps.updateUserWithAuth(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Ошибка обновления имени и почты при невалидном токене")
    public void failUpdateUserWithIncorrectTokenTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        String incorrectToken = accessToken+"abc";
        response = UserSteps.updateUserWithAuth(user, incorrectToken);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("invalid signature"));
    }
    @Test
    @DisplayName("Ошибка обновления имени и почты пользователя без авторизации")
    public void failUpdateUserWithoutTokenTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserSteps.updateUserWithoutAuth(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @Test
    @DisplayName("Обновить пароль пользователя")
    public void failUpdatePasswordUserTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserSteps.updateUserWithAuth(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200);

        response = UserSteps.loginUser(user);
        response.then().log().all().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Обновить пароля пользователя без авторизации")
    public void failUpdatePasswordUserWithoutToken() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserSteps.updateUserWithoutAuth(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
        response = UserSteps.loginUser(user);
        response.then().log().all().assertThat().statusCode(401);
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }}
