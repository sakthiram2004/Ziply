package com.sr.Ziply.repository;

import com.sr.Ziply.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
