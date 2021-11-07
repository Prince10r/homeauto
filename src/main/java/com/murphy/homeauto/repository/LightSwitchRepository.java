package com.murphy.homeauto.repository;

import com.murphy.homeauto.model.LightSwitch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LightSwitchRepository  extends CrudRepository<LightSwitch, Long> {

}
