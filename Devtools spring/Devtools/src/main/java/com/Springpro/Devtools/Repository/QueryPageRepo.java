package com.Springpro.Devtools.Repository;

import com.Springpro.Devtools.Entity.QueryPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryPageRepo extends JpaRepository<QueryPage, Integer> {
}
