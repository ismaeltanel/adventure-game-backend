package com.project.adventuregame.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long currentLocationId;
    private long gameSessionId;
    private boolean warning = false;

    public Player(String name, int startingLocationId, long gameSessionId) {
        this.name = name;
        this.currentLocationId = startingLocationId;
        this.gameSessionId = gameSessionId;
    }
}
