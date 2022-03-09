package com.demo.system.web.user;


import com.demo.system.model.Role;
import com.demo.system.model.User;
import com.demo.system.util.JsonUtil;
import com.demo.system.web.MatcherFactory;
import com.demo.system.web.restaurant.RestaurantTestData;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int R_ADMIN_ID = 3;
    public static final int NOT_FOUND = 100;
    public static final String USER_MAIL = "user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String R_ADMIN_MAIL = "r_admin@gmail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User r_admin = new User(R_ADMIN_ID, "Restaurants Admin", R_ADMIN_MAIL, "radmin", Role.R_ADMIN);

    static {
        r_admin.setAdminRestaurants(Set.of(RestaurantTestData.MAC_ID, RestaurantTestData.SHALYPIN_ID));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
