import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserSteps;

public class UserLoginTests {
    String accessToken;
    @Test
    @DisplayName("Авторизовать уже существующего пользователя")
    public void loginSuccessUserExistsTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response = UserSteps.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }

    @Test
    @DisplayName("Ошибка авторизации без ведения почты")
    public void failLoginUserExistsWithoutEmailTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setEmail(null);
        user.setName(null);
        response = UserSteps.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Ошибка авторизации без введения пароля")
    public void failLoginUserExistsWithoutPasswordTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword(null);
        user.setName(null);
        response = UserSteps.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Ошибка авторизации с невалидной почтой")
    public void failLoginUserNotExistsTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setEmail("abc1685" + user.getEmail());
        user.setName(null);
        response = UserSteps.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Ошибка авторизации с невалидным паролем")
    public void failLoginPasswordIncorrectTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("abc1685" + user.getPassword());
        user.setName(null);
        response = UserSteps.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
