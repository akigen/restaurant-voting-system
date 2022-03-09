package com.demo.system.web.restaurant;

import com.demo.system.model.MenuItem;
import com.demo.system.repository.MenuItemRepository;
import com.demo.system.service.MenuItemService;
import com.demo.system.util.validation.AdminRestaurantsUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.demo.system.util.validation.ValidationUtil.assureIdConsistent;
import static com.demo.system.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMenuItemController {

    static final String REST_URL =  AdminRestaurantsUtil.REST_URL + "/{restaurantId}/menu-items";

    private final MenuItemRepository repository;
    private final MenuItemService service;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get for restaurantId={}, id={}", restaurantId, id);
        return ResponseEntity.of(repository.get(restaurantId, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(evict = {
            @CacheEvict(value = "allRestaurantsWithMenu", allEntries = true),
            @CacheEvict(value = "restaurantWithMenu", key = "#restaurantId")
    })
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {} for restaurantId={}", id, restaurantId);
        MenuItem menuItem = repository.checkBelong(restaurantId, id);
        repository.delete(menuItem);
    }

    @GetMapping("/by-date")
    public List<MenuItem> getByRestaurantAndDate(@PathVariable int restaurantId, @Nullable @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) date = LocalDate.now();
        log.info("getByRestaurantAndDate for restaurantId={} and date={}", restaurantId, date);
        return repository.getByRestaurantAndDate(restaurantId, date);
    }

    @GetMapping
    public List<MenuItem> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant for restaurantId={}", restaurantId);
        return repository.getByRestaurant(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Caching(evict = {
            @CacheEvict(value = "allRestaurantsWithMenu", allEntries = true, condition = "T(java.time.LocalDate).now().equals(#menuItem.actualDate)"),
            @CacheEvict(value = "restaurantWithMenu", key = "#restaurantId", condition = "T(java.time.LocalDate).now().equals(#menuItem.actualDate)")
    })
    public ResponseEntity<MenuItem> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody MenuItem menuItem) {
        log.info("create {} for restaurantId={}", menuItem, restaurantId);
        checkNew(menuItem);
        MenuItem created = service.save(restaurantId, menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(evict = {
            @CacheEvict(value = "allRestaurantsWithMenu", allEntries = true, condition = "T(java.time.LocalDate).now().equals(#menuItem.actualDate)"),
            @CacheEvict(value = "restaurantWithMenu", key = "#restaurantId", condition = "T(java.time.LocalDate).now().equals(#menuItem.actualDate)")
    })
    public void update(@PathVariable int restaurantId, @Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        log.info("update {} for restaurantId={}, id={}", menuItem, restaurantId, id);
        assureIdConsistent(menuItem, id);
        repository.checkBelong(restaurantId, id);
        service.save(restaurantId, menuItem);
    }
}
