package com.project.adventuregame.service;

import com.project.adventuregame.entity.Enemy;
import com.project.adventuregame.entity.Item;
import com.project.adventuregame.entity.Location;
import com.project.adventuregame.entity.Player;
import com.project.adventuregame.repository.EnemyRepository;
import com.project.adventuregame.repository.ItemRepository;
import com.project.adventuregame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EnemyService enemyService;
    @Autowired
    private EnemyRepository enemyRepository;

    public Player startNewGame(String playerName) {
        Player player = new Player(playerName,1,1);
        return playerRepository.save(player);
    }

    public String movePlayer(Long playerId, String direction) {
        direction = direction.toUpperCase();
        Player player = playerRepository.findById(playerId).orElseThrow();

        if(locationService.isValidMove(player.getCurrentLocationId(), direction)){
            long destination = locationService.getDestinationLocationId(player.getCurrentLocationId(), direction);
            List<Enemy> enemies = enemyService.getEnemiesFromLocation(destination);

            if(!enemies.isEmpty()) {
                if (!player.isWarning()) {
                    player.setWarning(true);
                    playerRepository.save(player);
                    return "There's an enemy ahead. Do you want to continue?";
                } else {
                    player.setWarning(false);
                    playerRepository.save(player);
                }

                return enemyService.enemyEncounter(playerId, enemies.getFirst().getId());
            }

            if(destination == 3L){

                List<Item> inventory = itemService.getItemsFromPlayer(playerId);
                boolean hasLight = inventory.stream()
                        .anyMatch(item -> item.getType().equals(Item.ItemType.LIGHT_SOURCE));

                if (!hasLight) {
                    if (!player.isWarning()) {
                        player.setWarning(true);
                        playerRepository.save(player);
                        return "Are you sure you want to continue without a light? Click again to continue.";
                    } else {
                        player.setWarning(false);
                        playerRepository.save(player);
                    }
                }

                String result = dangerAreaEntry(playerId);
                if(result.contains("died")) {
                    return result;
                }

                player.setCurrentLocationId(destination);
                playerRepository.save(player);
                return result;
            }



            player.setCurrentLocationId(destination);
            playerRepository.save(player);
            return locationService.getLocationById(destination)
                    .map(location -> location.getDescription())
                    .orElse("Location not found");
        }else {
            return "You cannot go in that direction";
        }

    }

    public String dangerAreaEntry(long playerId) {
        List<Item> inventory = itemService.getItemsFromPlayer(playerId);


        boolean hasLight = inventory
                .stream()
                .anyMatch(item -> item.getType().equals(Item.ItemType.LIGHT_SOURCE));

        if(!hasLight) {
            if(Math.random() < 0.95) {
                return "You fell into a chasm and died!";
            }
            return "It's dark and you can't see anything";
        }

        return "You found a hidden treasure!";

    }

    public String resetGame(long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        player.setCurrentLocationId(1L);
        playerRepository.save(player);
        itemRepository.deleteAll();
        enemyRepository.deleteAll();

        enemyService.initializeEnemies();
        List<Item> coinInCave = itemRepository.findByLocationId(2L);
        if(coinInCave.isEmpty()){
            Item coin = new Item("Coin", Item.ItemType.COIN,null,"A gold coin", null, 2L);
            itemRepository.save(coin);
        }

        return "Game reset successfully";
    }

    public Map<String, Object> getCurrentPlayerLocation(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        String description = locationService.getLocationById(player.getCurrentLocationId())
                .map(Location::getDescription)
                .orElse("Location not found");

        Map<String, Object> response = new HashMap<>();
        response.put("locationId", player.getCurrentLocationId());
        response.put("description", description);
        return response;
    }

    public Optional<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }


}
