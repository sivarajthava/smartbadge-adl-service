package com.smartbadge.adl.team;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeamRepository extends MongoRepository<TeamDocument, String> {
    Optional<TeamDocument> findByStaffId(String staffId);
    boolean existsByStaffId(String staffId);
}
