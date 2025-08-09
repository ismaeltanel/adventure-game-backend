package com.project.adventuregame.service;

import com.project.adventuregame.entity.Item;
import com.project.adventuregame.entity.Location;
import com.project.adventuregame.entity.Player;
import com.project.adventuregame.repository.ItemRepository;
import com.project.adventuregame.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DependsOn("locationService")
public class ItemService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @PostConstruct
    public void initializeItems() {
        if(itemRepository.findAll().isEmpty()) {
            Location cave = locationService.getLocationById(2L).orElseThrow();
            Item coin = new Item("Coin","COIN","A gold coin", null, cave.getLocationId());
            itemRepository.save(coin);
        }
    }

    public List<Item> getItemsFromLocation(Long locationId) {
        return itemRepository.findByLocationId(locationId);
    }

    public List<Item> getItemsFromPlayer(Long playerId) {
        return itemRepository.findByPlayerId(playerId);
    }

    public String pickUpItems(long itemId, long playerId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        if(item.getLocationId() != null && item.getPlayerId() == null) {
            item.setLocationId(null);
            item.setPlayerId(playerId);
            itemRepository.save(item);
            return "Picked up " + item.getName();
        }
        return item.getName() + " not available";
    }

    public String dropItems(long itemId, long playerId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();

        if(item.getLocationId() == null && item.getPlayerId().equals(playerId)) {
            item.setLocationId((long) player.getCurrentLocationId());
            item.setPlayerId(null);
            itemRepository.save(item);
            return "Dropped " + item.getName();
        }
        return item.getName() + " can't be dropped";
    }

    public String buyItem(long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        Location shop = locationService.getLocationById(6L).orElseThrow();
        List<Item> inventory = getItemsFromPlayer(playerId);

        if(player.getCurrentLocationId() != shop.getLocationId()) {
            return "You must be in the shop to buy items";
        }

        if(inventory.stream().noneMatch(item -> item.getType().equals("COIN"))){
            return "Need a coin to buy light";
        }


        Item coin = inventory
                .stream()
                .filter(item -> item.getType().equals("COIN"))
                .findFirst()
                .orElseThrow();

        itemRepository.delete(coin);
        Item light = new Item("Light","LIGHT_SOURCE","A small glowing light", playerId, null);
        itemRepository.save(light);
        return "Successfully bought " + light.getName();

    }
}
