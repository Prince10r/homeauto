package com.murphy.homeauto.repository;

import com.murphy.homeauto.model.PlugEnergy;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlugEnergyRepository extends CrudRepository<PlugEnergy, Long> {

    @Query("select pe.* from plug_energy pe join (select plug, MAX(sample_time) maxse from plug_energy group by plug) maxpe on (pe.plug = maxpe.plug and pe.sample_time = maxpe.maxse)")
    public List<PlugEnergy> findLatestPlugEnergies();

}
