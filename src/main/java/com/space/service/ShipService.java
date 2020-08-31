package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ShipService {
    List<Ship> getAllShipsWithSpec(Specification<Ship> specification, Pageable pageable);
    Long getCount(Specification<Ship> specification);

    Ship create(Ship ship);

    Ship getShip(Long id);

    Ship updateShip(Long id, Ship ship);

    void deleteShip(Long id);
}
