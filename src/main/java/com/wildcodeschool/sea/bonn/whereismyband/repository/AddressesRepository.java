package com.wildcodeschool.sea.bonn.whereismyband.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Addresses;

public interface AddressesRepository extends JpaRepository<Addresses, Long> {

}
