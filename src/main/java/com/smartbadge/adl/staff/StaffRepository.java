package com.smartbadge.adl.staff;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StaffRepository extends MongoRepository<Staff, String> {
    Optional<Staff> findByEmail(String email);
    Optional<Staff> findByStaffId(String staffId);
    Page<Staff> findAll(Pageable pageable);
    boolean existsByEmail(String email);
}
