package com.ssafy.db.repository;

import com.ssafy.db.entity.UserConference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConferenceRepository extends JpaRepository<UserConference, Long> {
}
