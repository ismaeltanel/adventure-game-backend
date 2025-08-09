package com.project.adventuregame.controller;

import com.project.adventuregame.entity.Item;
import com.project.adventuregame.entity.Player;
import com.project.adventuregame.service.GameService;
import com.project.adventuregame.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ItemService itemService;

    @PostMapping("/game/start")
    public Player startGame(@RequestParam String playerName) {
        return gameService.startNewGame(playerName);
    }

    @PostMapping("/game/{playerId}/move/{direction}")
    public String movePlayer(@PathVariable Long playerId, @PathVariable String direction) {
        return gameService.movePlayer(playerId, direction);
    }

    @GetMapping("/game/{playerId}/inventory")
    public List<Item> getItemsFromPlayer(@PathVariable Long playerId) {
        return itemService.getItemsFromPlayer(playerId);
    }

    @GetMapping("/game/location/{locationId}/items")
    public List<Item> getItemsFromLocation(@PathVariable Long locationId) {
        return itemService.getItemsFromLocation(locationId);
    }

    @PostMapping("/game/{playerId}/pickup/{itemId}")
    public String pickUpItems(@PathVariable long itemId, @PathVariable long playerId){
        return itemService.pickUpItems(itemId,playerId);
    }

    @PostMapping("/game/{playerId}/drop/{itemId}")
    public String dropItems(@PathVariable long itemId, @PathVariable long playerId){
        return itemService.dropItems(itemId,playerId);
    }

    @PostMapping("/game/{playerId}/shop/buy")
    public String buyItem(@PathVariable long playerId) {
        return itemService.buyItem(playerId);
    }

    @PostMapping("/game/{playerId}/reset")
    public String resetGame(@PathVariable long playerId) {
        return gameService.resetGame(playerId);
    }

    @GetMapping("/game/{playerId}/current/location")
    public Map<String, Object> getCurrentLocation(@PathVariable Long playerId) {
        return gameService.getCurrentPlayerLocation(playerId);
    }
}
