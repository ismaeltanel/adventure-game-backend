package com.project.adventuregame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    private long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    private String description;
    private Long playerId;
    private Long locationId;

    public enum ItemType {
        LIGHT_SOURCE,
        COIN,
        WEAPON
    }

    public enum WeaponType {
        SWORD,
        AXE,
        MACE,
        HOLY,
        FIRE,
        ICE,
        MAGIC,
        RANGED
    }

    public Item(String name, ItemType type, WeaponType weaponType, String description, Long playerId, Long locationId) {
        this.name = name;
        this.type = type;
        this.weaponType = weaponType;
        this.description = description;
        this.playerId = playerId;
        this.locationId = locationId;
    }
}
