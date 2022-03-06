package com.demo.system.web.restaurant;

import com.demo.system.View;
import com.demo.system.model.Restaurant;
import com.demo.system.repository.RestaurantRepository;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<Restaurant> getAllEnabled() {
        log.info("getAllEnabled");
        return repository.getAllEnabled();
    }

    @GetMapping("/menu_today")
    @JsonView(View.RestaurantWithMeals.class)
    public List<Restaurant> getWithMenuForToday() {
        log.info("getWithMenuForToday");
        return repository.getWithMenuByDate(LocalDate.now());
    }


    @GetMapping("/{id}/menu_today")
    @JsonView(View.RestaurantWithMeals.class)
    public Restaurant getWithMenuByRestaurantForToday(@PathVariable int id) {
        log.info("getWithMenuByRestaurantForToday {}", id);
        repository.checkAvailable(id);
        return repository.getWithMenuByRestaurantAndDate(id, LocalDate.now());
    }
}