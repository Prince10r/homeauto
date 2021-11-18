package com.murphy.homeauto.repository;

import com.murphy.homeauto.model.Plug;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlugRepository extends CrudRepository<Plug, Long> {

    @Query("select * from plug p where p.name = :name")
    public Plug findByName(String name);
}
