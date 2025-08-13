package com.project.adventuregame.service;

import com.project.adventuregame.entity.Exit;
import com.project.adventuregame.entity.Location;
import com.project.adventuregame.repository.ExitRepository;
import com.project.adventuregame.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ExitRepository exitRepository;

    @PostConstruct
    public void initializeLocations() {

        if(locationRepository.findAll().isEmpty()) {
            //locations
            locationRepository.save(new Location(1, "You have entered the forest"));
            locationRepository.save(new Location(2, "You wandered into a cave"));
            locationRepository.save(new Location(3, "You have stepped deeper into the cave"));
            locationRepository.save(new Location(4, "You entered a cozy village"));
            locationRepository.save(new Location(5, "You ended up on top of a hill"));
            locationRepository.save(new Location(6, "You are inside a general shop"));

            //exits
            //location 1 Forest
            exitRepository.save(new Exit(1,"W",2));
            exitRepository.save(new Exit(1,"N",4));
            exitRepository.save(new Exit(1,"E",5));

            //location 2 Cave
            exitRepository.save(new Exit(2,"E",1));
            exitRepository.save(new Exit(2,"W",3));

            //location 3 Deep Cave
            exitRepository.save(new Exit(3,"E",2));

            //location 4 Village
            exitRepository.save(new Exit(4,"N",6));
            exitRepository.save(new Exit(4,"S",1));

            //location 5 Hill
            exitRepository.save(new Exit(5,"W",1));

            //location 6 Shop
            exitRepository.save(new Exit(6,"S",4));

        }
    }

    public Optional<Location> getLocationById(long locationId) {
        return locationRepository.findById(locationId);
    }

    public List<Exit> getExitsFromLocation(int locationId) {
        return exitRepository.findBySourceLocationId(locationId);
    }

    public boolean isValidMove(long currentLocationId, String direction) {
        return exitRepository.findBySourceLocationId(currentLocationId)
                .stream()
                .anyMatch(exit -> exit.getDirection().equals(direction));

    }

    public long getDestinationLocationId(long currentLocationId, String direction) {
        return  exitRepository.findBySourceLocationId(currentLocationId)
                .stream()
                .filter(exit -> exit.getDirection().equals(direction))
                .findFirst()
                .map(exit -> exit.getDestinationLocationId())
                .orElseThrow();
    }
}
