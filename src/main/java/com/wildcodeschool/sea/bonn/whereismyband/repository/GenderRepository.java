package com.wildcodeschool.sea.bonn.whereismyband.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

}
