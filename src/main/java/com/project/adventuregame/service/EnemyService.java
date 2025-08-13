package com.project.adventuregame.service;

import com.project.adventuregame.entity.Enemy;
import com.project.adventuregame.entity.Item;
import com.project.adventuregame.entity.Location;
import com.project.adventuregame.entity.Player;
import com.project.adventuregame.repository.EnemyRepository;
import com.project.adventuregame.repository.ItemRepository;
import com.project.adventuregame.repository.LocationRepository;
import com.project.adventuregame.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EnemyService {

    @Autowired
    private EnemyRepository enemyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;


    @PostConstruct
    public void initializeEnemies() {
        if(enemyRepository.findAll().isEmpty()){
            Enemy goblin = new Enemy("Goblin", 5L, 30L, 20L, Enemy.EnemyType.BEAST);
            enemyRepository.save(goblin);
        }
    }

    public String enemyEncounter(long playerId, long enemyId) {
        List<Item> inventory = itemRepository.findByPlayerId(playerId);
        Enemy enemy = enemyRepository.findById(enemyId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        Location location = locationRepository.findById(player.getCurrentLocationId()).orElseThrow();


        Item bestWeapon = inventory
                .stream()
                .filter(item -> item.getType().equals(Item.ItemType.WEAPON))
                .max(Comparator.comparingLong(item -> getWeaponPower(item.getWeaponType()) +
                        calculateTypeEffectiveness(item.getWeaponType(), enemy.getType())))
                .orElse(null);
        long winChance = 50 + (bestWeapon != null ? getWeaponPower(bestWeapon.getWeaponType()) +
                calculateTypeEffectiveness(bestWeapon.getWeaponType(), enemy.getType()) : 0) - enemy.getDifficulty();
        if (Math.random() < (winChance / 100.0)){
            player.setCurrentLocationId(enemy.getLocationId());
            playerRepository.save(player);

            Location newLocation = locationRepository.findById(enemy.getLocationId()).orElseThrow();

            enemyRepository.delete(enemy);
            String dropMessage = handleItemDrop(enemy, enemy.getLocationId());
            return "Successfully killed " + enemy.getName() + " with " +
                    (bestWeapon != null ? bestWeapon.getName() : "your fists. ") +
                    dropMessage + newLocation.getDescription();
        } else {
            if(!inventory.isEmpty()) {
                Item randomItem = inventory.get((int) (Math.random() * inventory.size()));
                randomItem.setPlayerId(null);
                randomItem.setLocationId(enemy.getLocationId());
                itemRepository.save(randomItem);
                return "The " + enemy.getName() + " defeated you. You dropped your " +
                        randomItem.getName() + ". " + location.getDescription();
            }
            return "The " + enemy.getName() + " defeated you. You had nothing to steal. " +
                     location.getDescription();
        }
    }

    public List<Enemy> getEnemiesFromLocation(Long locationId) {
        return enemyRepository.findByLocationId(locationId);
    }

    private long calculateTypeEffectiveness(Item.WeaponType weapon, Enemy.EnemyType enemy) {
        switch (weapon) {
            case HOLY -> {
                switch (enemy){
                    case UNDEAD -> {
                        return 20L;
                    }
                    case MAGICAL -> {
                        return 10L;
                    }
                    case BEAST -> {
                        return 5L;
                    }
                    case ARMORED, HUMAN -> {
                        return 0L;
                    }
                }
            }
            case MACE -> {
                switch (enemy) {
                    case ARMORED -> {
                        return 15L;
                    }
                    case UNDEAD -> {
                        return 5L;
                    }
                    case MAGICAL, BEAST, HUMAN -> {
                        return 0L;
                    }
                }
            }
            case SWORD -> {
                switch (enemy) {
                    case ARMORED -> {
                        return -10L;
                    }
                    case BEAST -> {
                        return 5L;
                    }
                    case UNDEAD, MAGICAL, HUMAN -> {
                        return 0L;
                    }
                }
            }
            case MAGIC -> {
                switch (enemy) {
                    case UNDEAD -> {
                        return 10L;
                    }
                    case ARMORED -> {
                        return 20L;
                    }
                    case MAGICAL -> {
                        return -15L;
                    }
                    case BEAST, HUMAN -> {
                        return 0L;
                    }
                }
            }
            case FIRE -> {
                switch (enemy) {
                    case HUMAN, BEAST -> {
                        return 20L;
                    }
                    case UNDEAD, ARMORED, MAGICAL -> {
                        return 0L;
                    }
                }
            }
            case ICE -> {
                switch (enemy) {
                    case HUMAN, ARMORED -> {
                        return 20L;
                    }

                    case BEAST, UNDEAD, MAGICAL -> {
                        return 0L;
                    }
                }
            }
            case RANGED -> {
                switch (enemy) {
                    case HUMAN -> {
                        return 10L;
                    }
                    case MAGICAL -> {
                        return 5L;
                    }
                    case ARMORED -> {
                        return -15L;
                    }
                    case BEAST, UNDEAD-> {
                        return 0L;
                    }
                }
            }
            case AXE -> {
                switch (enemy) {
                    case BEAST -> {
                        return 10L;
                    }
                    case HUMAN -> {
                        return 15L;
                    }
                    case MAGICAL -> {
                        return 5L;
                    }
                    case ARMORED -> {
                        return -5L;
                    }
                    case UNDEAD-> {
                        return 0L;
                    }
                }
            }
        }
        return 0L;
    }

    private String handleItemDrop(Enemy enemy, Long locationId) {
        if(Math.random() < (enemy.getDropChance() / 100.0)) {
            switch (enemy.getType()) {
                case BEAST -> {
                    if(Math.random() < 0.6) {
                        Item sword = new Item("Rusty Sword", Item.ItemType.WEAPON,
                                Item.WeaponType.SWORD, "A worn blade",
                                null, locationId);
                        itemRepository.save(sword);
                        return " The beast dropped a rusty sword. ";
                    }
                    else {
                        Item axe = new Item("Wooden Axe", Item.ItemType.WEAPON,
                                Item.WeaponType.AXE, "A crude axe",
                                null, locationId);
                        itemRepository.save(axe);
                        return " The beast dropped a wooden axe. ";
                    }
                }
                case ARMORED -> {

                }
            }
            return "";
        }
        return "";

    }

    private long getWeaponPower(Item.WeaponType weaponType) {
        if (weaponType == null) return 0L;
        switch (weaponType){
            case AXE -> {
                return 25L;
            }
            case SWORD -> {
                return 20L;
            }
            case MACE -> {
                return 30L;
            }
            case HOLY -> {
                return 35L;
            }
            case FIRE, ICE -> {
                return 40L;
            }
            case MAGIC -> {
                return 45L;
            }
            case RANGED -> {
                return 15L;
            }
        }

        return 0L;
    }
}
