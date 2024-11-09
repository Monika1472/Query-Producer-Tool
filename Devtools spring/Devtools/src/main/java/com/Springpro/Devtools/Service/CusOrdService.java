package com.Springpro.Devtools.Service;
import com.Springpro.Devtools.Entity.CusOrd;
import com.Springpro.Devtools.Repository.CusOrdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CusOrdService {
    @Autowired
    private CusOrdRepo cusOrdRepo;

    public List<CusOrd> getAllDetails(){
        return cusOrdRepo.findAll();
    }
}