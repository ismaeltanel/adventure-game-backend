package com.project.adventuregame.repository;

import com.project.adventuregame.entity.Exit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExitRepository extends JpaRepository<Exit, Long> {
    List<Exit> findBySourceLocationId(long sourceLocationId);
}
