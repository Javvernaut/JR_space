package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/ships")
    public List<Ship> getShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
            @RequestParam(value = "order", defaultValue = "ID") ShipOrder shipOrder

            ) {

        Specification<Ship> specification = (Specification<Ship>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (name != null)
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            if (planet != null)
                predicateList.add(criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
            if (shipType != null)
                predicateList.add(criteriaBuilder.equal(root.get("shipType"), shipType));
            if (after != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after)));
            if (before != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before)));
            if (isUsed != null)
                predicateList.add(criteriaBuilder.equal(root.get("isUsed"), isUsed));
            if (minSpeed != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed));
            if (maxSpeed != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed));
            if (minCrewSize != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
            if (maxCrewSize != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));
            if (minRating != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            if (maxRating != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        return shipService.getAllShipsWithSpec(specification, PageRequest.of(pageNumber, pageSize, Sort.by(shipOrder.getFieldName())));
    }

    @GetMapping("/ships/count")
    public Long getCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating

    ) {

        Specification<Ship> specification = (Specification<Ship>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (name != null)
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            if (planet != null)
                predicateList.add(criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
            if (shipType != null)
                predicateList.add(criteriaBuilder.equal(root.get("shipType"), shipType));
            if (after != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after)));
            if (before != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before)));
            if (isUsed != null)
                predicateList.add(criteriaBuilder.equal(root.get("isUsed"), isUsed));
            if (minSpeed != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed));
            if (maxSpeed != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed));
            if (minCrewSize != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
            if (maxCrewSize != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));
            if (minRating != null)
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            if (maxRating != null)
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        return shipService.getCount(specification);
    }

    @GetMapping("/ships/{id}")
    public Ship getShip(@PathVariable Long id) {
        return shipService.getShip(id);
    }

    @PostMapping("/ships")
    public Ship create(@RequestBody Ship ship)
    {
        return shipService.create(ship);
    }

    @PostMapping("/ships/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship) {
        return shipService.updateShip(id, ship);
    }
    @DeleteMapping("/ships/{id}")
    public void deleteShip(@PathVariable Long id) {
        shipService.deleteShip(id);
    }
}
