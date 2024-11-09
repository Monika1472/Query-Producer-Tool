package com.Springpro.Devtools.Controller;

import com.Springpro.Devtools.Entity.QueryPage;
import com.Springpro.Devtools.NotFoundException.ResourceNotFoundException;
import com.Springpro.Devtools.Repository.QueryPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/queryData")
public class QueryPageController {
    @Autowired
    private QueryPageRepo queryPageRepo;

    @GetMapping("/Queries")
    public List<QueryPage> getAllQueries(){
        return queryPageRepo.findAll();
    }
    @PostMapping("/Queries")
    public QueryPage createQuery(@RequestBody QueryPage queryPage){
        return queryPageRepo.save(queryPage);
    }
    @GetMapping("/Queries/{queryid}")
    public QueryPage getQueryById(@PathVariable int queryid){
        QueryPage queryPage = queryPageRepo.findById(queryid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + queryid));
        return queryPage;
    }

    @PutMapping("/Queries/{queryid}")
    public QueryPage updateQuery(@PathVariable int queryid, @RequestBody QueryPage updatedQueryPage){
        QueryPage queryPage=queryPageRepo.findById(queryid)
                .orElseThrow(() -> new ResourceNotFoundException("Query not exist with id : " + queryid));
        queryPage.setQueryid(updatedQueryPage.getQueryid());
        queryPage.setTitle(updatedQueryPage.getTitle());
        queryPage.setDescription(updatedQueryPage.getDescription());
        queryPage.setQuerytype(updatedQueryPage.getQuerytype());
        queryPage.setDbname(updatedQueryPage.getDbname());
        queryPage.setCollectionname(updatedQueryPage.getCollectionname());
        queryPage.setQuery(updatedQueryPage.getQuery());
        queryPage.setType_of_placeholders(updatedQueryPage.getType_of_placeholders());
        queryPage.setDatabaseid(updatedQueryPage.getDatabaseid());
        QueryPage resultUpdatedQuery=queryPageRepo.save(queryPage);
        return resultUpdatedQuery;
    }


    @DeleteMapping("/Queries/{queryid}")
    public String deleteQuery(@PathVariable int queryid){
        QueryPage queryPage=queryPageRepo.findById(queryid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + queryid));
        queryPageRepo.delete(queryPage);
        return "Query deleted successfully";
    }
}
