package com.Springpro.Devtools.Repository;

import com.Springpro.Devtools.Entity.CusOrd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CusOrdRepo extends JpaRepository<CusOrd,Integer> {
}
