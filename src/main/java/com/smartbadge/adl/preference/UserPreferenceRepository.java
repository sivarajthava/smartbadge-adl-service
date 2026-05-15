package com.smartbadge.adl.preference;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPreferenceRepository extends MongoRepository<UserPreferenceDocument, String> {
	Optional<UserPreferenceDocument> findByUserId(String userId);

	boolean existsByUserId(String userId);

	void deleteByUserId(String userId);
}
