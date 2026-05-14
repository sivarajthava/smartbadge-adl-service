package com.smartbadge.adl.leave;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LeaveRepository extends MongoRepository<LeaveDocument, String> {
    Optional<LeaveDocument> findByStaffId(String staffId);
    boolean existsByStaffId(String staffId);
}
