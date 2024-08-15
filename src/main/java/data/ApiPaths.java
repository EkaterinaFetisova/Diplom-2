package data;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiPaths {
    public static final String HOME_PAGE = "https://stellarburgers.nomoreparties.site";
    public static final String USER_REGISTER_PATH = "api/auth/register";
    public static final String USER_PATH = "api/auth/user";
    public static final String USER_AUTH_PATH = "/api/auth/login";
    public static final String ORDER_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";

    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOME_PAGE)
                .log()
                .all();

    }
}
