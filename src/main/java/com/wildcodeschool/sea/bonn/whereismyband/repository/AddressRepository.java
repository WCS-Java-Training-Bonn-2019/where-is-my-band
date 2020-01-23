package com.wildcodeschool.sea.bonn.whereismyband.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
