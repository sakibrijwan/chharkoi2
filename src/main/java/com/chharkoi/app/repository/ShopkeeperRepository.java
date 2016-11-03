package com.chharkoi.app.repository;

import com.chharkoi.app.domain.Shopkeeper;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shopkeeper entity.
 */
@SuppressWarnings("unused")
public interface ShopkeeperRepository extends JpaRepository<Shopkeeper,Long> {

    @Query("select shopkeeper from Shopkeeper shopkeeper where shopkeeper.user.login = ?#{principal.username}")
    Shopkeeper findByUserIsCurrentUser();

}
