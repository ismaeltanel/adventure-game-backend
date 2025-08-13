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
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Long locationId;
    private Long difficulty;
    private Long dropChance;

    @Enumerated(EnumType.STRING)
    private EnemyType type;

    public enum EnemyType {
        ARMORED,
        MAGICAL,
        HUMAN,
        UNDEAD,
        BEAST
    }

    public Enemy(String name, Long locationId, Long difficulty, Long dropChance, EnemyType type) {
        this.name = name;
        this.locationId = locationId;
        this.difficulty = difficulty;
        this.dropChance = dropChance;
        this.type = type;
    }
}
