package com.Springpro.Devtools.Repository;

import com.Springpro.Devtools.Entity.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRepo extends JpaRepository<Database, Integer> {
}
