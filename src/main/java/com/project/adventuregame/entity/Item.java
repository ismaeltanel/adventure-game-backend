package com.project.adventuregame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String type;
    private String description;
    private Long playerId;
    private Long locationId;

    public Item(String name, String type, String description, Long playerId, Long locationId) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.playerId = playerId;
        this.locationId = locationId;
    }
}
