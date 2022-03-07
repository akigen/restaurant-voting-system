package com.demo.system.util;

import com.demo.system.model.DishRef;
import com.demo.system.model.MenuItem;
import com.demo.system.model.Restaurant;
import com.demo.system.to.RestaurantWithMenu;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantWithMenu withMenu(Restaurant restaurant) {
        List<DishRef> dishRefs = restaurant.getMenuItems().stream().map(MenuItem::getDishRef).toList();
        return new RestaurantWithMenu(restaurant.id(), restaurant.getName(), restaurant.getAddress(), dishRefs);
    }

    public static List<RestaurantWithMenu> withMenu(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::withMenu).toList();
    }
}