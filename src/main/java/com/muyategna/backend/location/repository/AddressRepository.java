package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
