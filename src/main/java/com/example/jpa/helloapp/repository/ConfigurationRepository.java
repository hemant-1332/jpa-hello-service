package com.example.jpa.helloapp.repository;

import com.example.jpa.helloapp.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
    //Custom method ; gets implemented by Jpa itself
    List<Configuration> findBykey(String key);

    //Jpa's method. No need to implement
    List<Configuration> findAll();
    @Override
    long count();
}
