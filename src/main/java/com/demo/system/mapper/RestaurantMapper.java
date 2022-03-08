package com.demo.system.mapper;

import com.demo.system.model.DishRef;
import com.demo.system.model.MenuItem;
import com.demo.system.model.Restaurant;
import com.demo.system.to.RestaurantWithMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper extends BaseMapper<Restaurant, RestaurantWithMenu> {

    @Mapping(target = "dishRefs", expression = "java(getDishRefs(restaurant))")
    @Override
    RestaurantWithMenu toTo(Restaurant restaurant);

    default List<DishRef> getDishRefs(Restaurant restaurant) {
        return restaurant.getMenuItems().stream()
                .map(MenuItem::getDishRef).toList();
    }
}
