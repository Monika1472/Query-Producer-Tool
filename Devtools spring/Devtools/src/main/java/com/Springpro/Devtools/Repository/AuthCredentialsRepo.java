package com.Springpro.Devtools.Repository;

import com.Springpro.Devtools.Entity.AuthCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AuthCredentialsRepo extends JpaRepository<AuthCredentials, Integer> {
    AuthCredentials findByUsernameAndPassword(String username, String password);
    AuthCredentials findByUsername(String username);
    boolean existsByUsername(String username);
}
