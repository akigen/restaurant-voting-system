package com.demo.system.web.restaurant;

import com.demo.system.model.DishRef;
import com.demo.system.repository.DishRefRepository;
import com.demo.system.service.DishRefService;
import com.demo.system.util.validation.AdminRestaurantsUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.demo.system.util.validation.ValidationUtil.assureIdConsistent;
import static com.demo.system.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishRefController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminDishRefController {

    static final String REST_URL = AdminRestaurantsUtil.REST_URL + "/{restaurantId}/dish-ref";

    private final DishRefRepository repository;
    private final DishRefService service;

    @GetMapping("/{id}")
    public ResponseEntity<DishRef> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get for restaurantId={}, id={}", restaurantId, id);
        return ResponseEntity.of(repository.get(restaurantId, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // No cache evict: couldn't delete, if used in MenuItem
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete for restaurantId={}, id={}", restaurantId, id);
        DishRef dishRef = repository.checkBelong(restaurantId, id);
        repository.delete(dishRef);
    }

    @GetMapping
    public List<DishRef> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant for restaurantId={}", restaurantId);
        return repository.getByRestaurant(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishRef> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody DishRef dishRef) {
        log.info("create {} for restaurantId={}", dishRef, restaurantId);
        checkNew(dishRef);
        DishRef created = service.save(restaurantId, dishRef);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // https://stackoverflow.com/questions/25379051/548473
    @Caching(evict = {
            @CacheEvict(value = "allRestaurantsWithMenu", allEntries = true),
            @CacheEvict(value = "restaurantWithMenu", key = "#restaurantId")
    })
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody DishRef dishRef) {
        log.info("update {} for restaurantId={}, id={}", dishRef, restaurantId, id);
        assureIdConsistent(dishRef, id);
        repository.checkBelong(restaurantId, id);
        service.save(restaurantId, dishRef);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allRestaurantsWithMenu", allEntries = true),
            @CacheEvict(value = "restaurantWithMenu", key = "#restaurantId")
    })
    public void enable(@PathVariable int restaurantId, @PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        DishRef dishRef = repository.checkBelong(restaurantId, id);
        dishRef.setEnabled(enabled);
    }
}