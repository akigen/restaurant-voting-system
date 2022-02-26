package com.demo.system.service;

import com.demo.system.model.MenuItem;
import com.demo.system.repository.MenuItemRepository;
import com.demo.system.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MenuItemService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItem save(int restaurantId, MenuItem menuItem) {
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        return menuItemRepository.save(menuItem);
    }
}
