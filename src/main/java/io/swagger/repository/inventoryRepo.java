package io.swagger.repository;

import io.swagger.entity.InventoryEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@EnableJpaRepositories
@EntityScan
@Repository
public interface inventoryRepo extends JpaRepository<InventoryEntity,Long> {
    InventoryEntity findById(Integer id);

    InventoryEntity findByName(String name);


}
