package com.demo.system.repository;

import com.demo.system.error.DataConflictException;
import com.demo.system.model.DishRef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRefRepository extends BaseRepository<DishRef> {
    @Query("SELECT d FROM DishRef d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<DishRef> get(int restaurantId, int id);

    @Query("SELECT d FROM DishRef d WHERE d.restaurant.id=:restaurantId ORDER BY d.name ASC")
    List<DishRef> getByRestaurant(int restaurantId);

    default DishRef checkBelong(int restaurantId, int id) {
        return get(restaurantId, id).orElseThrow(
                () -> new DataConflictException("DishRef id=" + id + " doesn't belong to restaurant id=" + restaurantId));
    }
}
