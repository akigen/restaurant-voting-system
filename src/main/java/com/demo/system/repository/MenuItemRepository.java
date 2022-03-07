package com.demo.system.repository;

import com.demo.system.error.DataConflictException;
import com.demo.system.model.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId")
    void deleteByRestaurantId(int restaurantId);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.id=:id AND mi.restaurant.id=:restaurantId")
    Optional<MenuItem> get(int restaurantId, int id);

    @Query("SELECT mi from MenuItem mi WHERE mi.restaurant.id=:restaurantId AND mi.actualDate = :date ORDER BY mi.dishRef.name ASC")
    List<MenuItem> getByRestaurantAndDate(int restaurantId, LocalDate date);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId ORDER BY mi.actualDate DESC, mi.dishRef.name ASC")
    List<MenuItem> getByRestaurant(int restaurantId);

    default MenuItem checkBelong(int restaurantId, int id) {
        return get(restaurantId, id).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}
