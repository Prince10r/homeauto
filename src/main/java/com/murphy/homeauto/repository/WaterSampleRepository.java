package com.murphy.homeauto.repository;

import com.murphy.homeauto.model.WaterSample;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterSampleRepository extends CrudRepository<WaterSample, Long> {

    @Query("select * from water_sample w where w.date = (select max(date) from water_sample)")
    List<WaterSample> getLatestWaterTemps();
}
