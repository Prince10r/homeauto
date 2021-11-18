package com.murphy.homeauto.repository;

import com.murphy.homeauto.model.PlugEnergy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlugEnergyRepository extends CrudRepository<PlugEnergy, Long> {


}
