package com.Springpro.Devtools.Controller;

import com.Springpro.Devtools.Entity.Database;
import com.Springpro.Devtools.Service.DatabaseService;
import com.Springpro.Devtools.Service.MongoDBService;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class DatabaseController {

    private JdbcTemplate customJdbcTemplate;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private MongoDBService mongoDbService;

    private MongoClient mongoClient;

    @PreDestroy
    public void cleanUp() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    private MongoClient createMongoClient() {
        String connectionStringStr = mongoDbService.getCustomMongoConnectionString();
        return MongoClients.create(connectionStringStr);
    }

    @GetMapping("/getCredentials")
    public List<Database> getAllDB() {
        return databaseService.getAllDatabase();
    }

    @GetMapping("/getCredentialbyId/{databaseid}")
    public ResponseEntity<Database> getDBbyId(@PathVariable int databaseid) {
        try {
            Database credentials = databaseService.getDatabasebyId(databaseid);

            if ("MySQL".equalsIgnoreCase(credentials.getDatabasetype())) {
                configureMySQL(credentials);
            } else if ("MongoDB".equalsIgnoreCase(credentials.getDatabasetype())) {
                configureMongoDB(credentials);
            }

            return ResponseEntity.ok(credentials);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void configureMySQL(Database credentials) {
        DriverManagerDataSource customDataSource = new DriverManagerDataSource();
        customDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        customDataSource.setUrl("jdbc:mysql://" + credentials.getHostname() + ":" + credentials.getPortno() + "/" + credentials.getDbname());
        customDataSource.setUsername(credentials.getUsername());
        customDataSource.setPassword(credentials.getPassword());
        JdbcTemplate tempJdbcTemplate = new JdbcTemplate(customDataSource);
        tempJdbcTemplate.execute("SELECT 1");
        customJdbcTemplate = new JdbcTemplate(customDataSource);
        databaseService.setCustomJdbcTemplate(customJdbcTemplate);
        System.out.println(customDataSource.getUrl());
    }

    private void configureMongoDB(Database credentials) {
        mongoDbService.setCustomMongoConnectionString(credentials.getHostname() + credentials.getDbname());
        MongoClient tempMongoClient = createMongoClient();
        tempMongoClient.listDatabaseNames().first();
        mongoDbService.setCustomMongoDBName(credentials.getDbname());
        mongoClient = createMongoClient();
    }


    @PostMapping("/connect")
    public ResponseEntity<String> connectToDatabase(@RequestBody Database credentials) {
        try {
            if ("MySQL".equalsIgnoreCase(credentials.getDatabasetype())) {
                return connectToMySQL(credentials);
            } else if ("MongoDB".equalsIgnoreCase(credentials.getDatabasetype())) {
                return connectToMongoDB(credentials);
            } else {
                return ResponseEntity.badRequest().body("Error! Invalid database type provided");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Connection failed. " + e.getMessage());
        }
    }

    private ResponseEntity<String> connectToMySQL(Database credentials) {
        try {
            DriverManagerDataSource customDataSource = new DriverManagerDataSource();
            customDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            customDataSource.setUrl("jdbc:mysql://" + credentials.getHostname() + ":" + credentials.getPortno() + "/" + credentials.getDbname());
            customDataSource.setUsername(credentials.getUsername());
            customDataSource.setPassword(credentials.getPassword());
            JdbcTemplate tempJdbcTemplate = new JdbcTemplate(customDataSource);
            tempJdbcTemplate.execute("SELECT 1");
            customJdbcTemplate = new JdbcTemplate(customDataSource);
            databaseService.setCustomJdbcTemplate(customJdbcTemplate);
            databaseService.saveDatabase(credentials);
            System.out.println(customDataSource.getUrl());
            return ResponseEntity.ok("Connected to MySQL successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to connect to MySQL. " + e.getMessage());
        }
    }


    private ResponseEntity<String> connectToMongoDB(Database credentials) {
        String connectionStringStr = credentials.getHostname() + credentials.getDbname();
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(connectionStringStr);
            ListDatabasesIterable<Document> databases = mongoClient.listDatabases();
            for (Document db : databases) {
                if (db.getString("name").equals(credentials.getDbname())) {
                    System.out.println("Connected to MongoDB successfully");
                    mongoDbService.setCustomMongoConnectionString(connectionStringStr);
                    mongoDbService.setCustomMongoDBName(credentials.getDbname());
                    databaseService.saveDatabase(credentials);
                    return ResponseEntity.ok("Connected to MongoDB successfully");
                }
            }
            return ResponseEntity.badRequest().body("Database does not exist: " + credentials.getDbname());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Connection to MongoDB failed: " + e.getMessage());
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }



    @GetMapping("/sql/Queries/executeQuery/{query}")
    public ResponseEntity<List<Map<String, Object>>> executeQuery(@PathVariable String query) {
        if (customJdbcTemplate == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Map<String, Object>> result = customJdbcTemplate.queryForList(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/getCollections")
    public ResponseEntity<List<String>> getCollections() {
        try {
            // Reuse the existing MongoClient instance
            MongoDatabase database = mongoClient.getDatabase(mongoDbService.getCustomMongoDBName());
            List<String> collections = new ArrayList<>();
            for (String collectionName : database.listCollectionNames()) {
                collections.add(collectionName);
            }
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mongo/collections/getDocuments/{collectionName}")
    public ResponseEntity<List<String>> getDocuments(@PathVariable String collectionName) {
        try {
            // Reuse the existing MongoClient instance
            MongoDatabase database = mongoClient.getDatabase(mongoDbService.getCustomMongoDBName());
            MongoCollection<Document> collection = database.getCollection(collectionName);
            List<String> details = new ArrayList<>();
            for (Document document : collection.find()) {
                details.add(document.toJson());
            }
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mongo/executeQuery/{collectionName}/{query}")
    public ResponseEntity<List<String>> executeMongoQuery(@PathVariable String collectionName, @PathVariable String query) {
        try {
            // Reuse the existing MongoClient instance
            MongoDatabase database = mongoClient.getDatabase(mongoDbService.getCustomMongoDBName());
            MongoCollection<Document> collection = database.getCollection(collectionName);
            List<String> results = new ArrayList<>();
            for (Document document : collection.find(Document.parse(query))) {
                results.add(document.toJson());
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
