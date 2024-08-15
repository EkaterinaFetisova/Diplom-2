import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserSteps;

public class UserCreateTests {
    String accessToken;

    @Test
    @DisplayName("Создать пользователя")
    public void createSuccessUserTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Ошибка создания уже зарегистрированного пользователя")
    public void failCreateDoubleUserTest() {
        User user = UserSteps.createNewUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response = UserSteps.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }
    @Test
    @DisplayName("Ошибка создания пользователя без поля Email")
    public void failCreateUserWithoutEmailTest() {
        User user = UserSteps.createNewUser();
        user.setEmail(null);
        Response response = UserSteps.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Ошибка создания пользователя без поля Password")
    public void failCreateUserWithoutPasswordTest() {
        User user = UserSteps.createNewUser();
        user.setPassword(null);
        Response response = UserSteps.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Ошибка создания пользователя без поля Name")
    public void failCreateUserWithoutNameTest() {
        User user = UserSteps.createNewUser();
        user.setName(null);
        Response response = UserSteps.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }}
