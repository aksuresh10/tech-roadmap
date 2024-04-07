package com.techroadmap.techroadmap.repository;


import com.techroadmap.techroadmap.Entity.RoadMapObjectDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadMapRepository extends CrudRepository<RoadMapObjectDetails, String> {

}