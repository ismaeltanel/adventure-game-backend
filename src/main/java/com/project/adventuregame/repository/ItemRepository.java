package com.project.adventuregame.repository;

import com.project.adventuregame.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByLocationId(Long locationId);
    List<Item> findByPlayerId(Long playerId);
    List<Item> findByType(String type);
}
