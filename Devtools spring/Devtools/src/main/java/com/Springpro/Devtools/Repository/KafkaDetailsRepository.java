package com.Springpro.Devtools.Repository;

import com.Springpro.Devtools.Entity.KafkaDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaDetailsRepository extends JpaRepository<KafkaDetails, Long> {
}
