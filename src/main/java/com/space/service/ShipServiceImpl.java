package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {
    private ShipRepo shipRepo;

    @Autowired
    public ShipServiceImpl(ShipRepo shipRepo) {
        this.shipRepo = shipRepo;
    }

    @Override
    public List<Ship> getAllShipsWithSpec(Specification<Ship> specification, Pageable pageable) {
        return shipRepo.findAll(specification, pageable).getContent();
    }

    @Override
    public Long getCount(Specification<Ship> specification) {
        return shipRepo.count(specification);
    }

    @Override
    public Ship create(Ship ship) {
        if (ship.getName() !=null && ship.getPlanet() != null && ship.getShipType() != null && ship.getProdDate() !=null && ship.getSpeed() != null && ship.getCrewSize() != null
            &&!ship.getName().equals("") && ship.getName().length() <=50 && !ship.getPlanet().equals("") && ship.getPlanet().length() <=50
            && ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99 && ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999
            && (ship.getProdDate().getYear() + 1900) >= 2800 && (ship.getProdDate().getYear() + 1900) <= 3019) {

            if (null == ship.isUsed())
                ship.setUsed(false);

            ship.setRating(getShipRating(ship));

            return shipRepo.save(ship);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Ship getShip(Long id) {
        if (id > 0) {
            Optional<Ship> opShip = shipRepo.findById(id);
            if (opShip.isPresent())
                return opShip.get();
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {
        if (null != id && id > 0) {

            Optional<Ship> opShip = shipRepo.findById(id);

            if (opShip.isPresent()) {
                Ship updatableShip = opShip.get();
                if (ship.getName() != null)
                    if (!ship.getName().equals("") && ship.getName().length() <=50)
                        updatableShip.setName(ship.getName());
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                if (ship.getPlanet() != null)
                    if (!ship.getPlanet().equals("") && ship.getPlanet().length() <=50)
                        updatableShip.setPlanet(ship.getPlanet());
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                if (ship.getShipType() != null)
                    updatableShip.setShipType(ship.getShipType());

                if (ship.getProdDate() != null)
                    if ((ship.getProdDate().getYear() + 1900) >= 2800 && (ship.getProdDate().getYear() + 1900) <= 3019)
                        updatableShip.setProdDate(ship.getProdDate());
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                if (null != ship.isUsed())
                    updatableShip.setUsed(ship.isUsed());

                if (ship.getSpeed() != null)
                    if (ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99)
                        updatableShip.setSpeed(ship.getSpeed());
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                if (ship.getCrewSize() != null)
                    if (ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999)
                        updatableShip.setCrewSize(ship.getCrewSize());
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                updatableShip.setRating(getShipRating(updatableShip));

                return shipRepo.save(updatableShip);
            }
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private Double getShipRating(Ship ship) {
        Double k = ship.isUsed() ? 0.5 : 1.0;

        Calendar calendar = new GregorianCalendar();

        calendar.setTime(ship.getProdDate());

        int year = calendar.get(Calendar.YEAR);

        Double fullRating = 80 * ship.getSpeed() * k / (3019 - year + 1);

        return new BigDecimal(fullRating).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public void deleteShip(Long id) {
        if (null != id && id > 0)
            try {
                shipRepo.deleteById(id);

            } catch (EmptyResultDataAccessException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
