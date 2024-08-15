package data;
import org.apache.commons.lang3.RandomStringUtils;
import user.User;

public class UserRandomizer {
    public static User getRandomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(10).toLowerCase() + "@yandex.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(6));
    }
}
