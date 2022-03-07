package com.demo.system.service;

import com.demo.system.model.DishRef;
import com.demo.system.repository.DishRefRepository;
import com.demo.system.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DishRefService {
    private final RestaurantRepository restaurantRepository;
    private final DishRefRepository dishRefRepository;

    @Transactional
    public DishRef save(int restaurantId, DishRef dishRef) {
        dishRef.setRestaurant(restaurantRepository.getById(restaurantId));
        return dishRefRepository.save(dishRef);
    }
}