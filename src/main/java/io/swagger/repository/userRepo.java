package io.swagger.repository;


import io.swagger.entity.UserEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@EntityScan
@Repository
public interface userRepo extends JpaRepository<UserEntity,Integer> {
    UserEntity findByUserId(String userId);

    UserEntity findByUserName(String userName);

}
