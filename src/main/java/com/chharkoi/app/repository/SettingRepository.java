package com.chharkoi.app.repository;

import com.chharkoi.app.domain.Setting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Setting entity.
 */
@SuppressWarnings("unused")
public interface SettingRepository extends JpaRepository<Setting,Long> {
    Setting findOneByName(String name);
}
