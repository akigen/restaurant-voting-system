package com.demo.system.repository;

import com.demo.system.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
