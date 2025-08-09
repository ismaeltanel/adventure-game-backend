package com.project.adventuregame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class Exit {

    @Id
    @GeneratedValue
    private long id;
    private int sourceLocationId;
    private String direction;
    private int destinationLocationId;

    public Exit(int sourceLocationId, String direction, int destinationLocationId) {
        this.sourceLocationId = sourceLocationId;
        this.direction = direction;
        this.destinationLocationId = destinationLocationId;

    }
}
