package com.demo.system.web.restaurant;

import com.demo.system.model.Restaurant;
import com.demo.system.repository.RestaurantRepository;
import com.demo.system.to.RestaurantWithMenu;
import com.demo.system.util.RestaurantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;

    @GetMapping
    @Cacheable("restaurants")
    public List<Restaurant> getAllEnabled() {
        log.info("getAllEnabled");
        return repository.getAllEnabled();
    }

    @GetMapping("/menu_today")
    @Cacheable("allRestaurantsWithMenu")
    public List<RestaurantWithMenu> getWithMenuForToday() {
        log.info("getWithMenuForToday");
        List<Restaurant> restaurants = repository.getWithMenuByDate(LocalDate.now());
        return RestaurantUtil.withMenu(restaurants);
    }


    @GetMapping("/{id}/menu_today")
    @Cacheable("restaurantWithMenu")
    public RestaurantWithMenu getWithMenuByRestaurantForToday(@PathVariable int id) {
        log.info("getWithMenuByRestaurantForToday {}", id);
        repository.checkAvailable(id);
        Restaurant restaurant = repository.getWithMenuByRestaurantAndDate(id, LocalDate.now());
        return RestaurantUtil.withMenu(restaurant);
    }
}