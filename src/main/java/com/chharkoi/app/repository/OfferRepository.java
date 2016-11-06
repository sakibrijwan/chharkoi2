package com.chharkoi.app.repository;

import com.chharkoi.app.domain.Offer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Offer entity.
 */
@SuppressWarnings("unused")
public interface OfferRepository extends JpaRepository<Offer,Long> {
    @Query("select offer from Offer offer where shopkeeper.user.login = ?#{principal.username} AND offer.endDate>=CURRENT_DATE()")
    Page<Offer> findOfferByLogin(String currentUserLogin, Pageable pageable);
}
