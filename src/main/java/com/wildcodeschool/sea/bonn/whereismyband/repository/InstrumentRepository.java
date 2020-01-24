package com.wildcodeschool.sea.bonn.whereismyband.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long>{

}
