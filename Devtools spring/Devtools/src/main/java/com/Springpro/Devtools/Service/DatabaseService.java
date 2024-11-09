package com.Springpro.Devtools.Service;

import com.Springpro.Devtools.Entity.Database;
import com.Springpro.Devtools.NotFoundException.ResourceNotFoundException;
import com.Springpro.Devtools.Repository.DatabaseRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    @Autowired
    private DatabaseRepo databaseRepo;

    @Getter
    private JdbcTemplate customJdbcTemplate;

    public Database saveDatabase(Database database){
        return databaseRepo.save(database);
    }

    public List<Database> getAllDatabase(){
        return databaseRepo.findAll();
    }
    public Database getDatabasebyId(int databaseid){
        Database database=databaseRepo.findById(databaseid)
            .orElseThrow(() -> new ResourceNotFoundException("Connection not exist with id :" + databaseid));
        return database;
    }
    public void setCustomJdbcTemplate(JdbcTemplate customJdbcTemplate) {
        this.customJdbcTemplate = customJdbcTemplate;
    }


}
