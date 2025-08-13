package com.project.adventuregame.repository;

import com.project.adventuregame.entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy, Long> {
    List<Enemy> findByLocationId(Long locationId);
}
