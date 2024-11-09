package com.Springpro.Devtools.Controller;

import com.Springpro.Devtools.Entity.CusOrd;
import com.Springpro.Devtools.Service.CusOrdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CusOrdController {
    @Autowired
    private CusOrdService cusOrdService;

    @GetMapping("/getAll")
    public List<CusOrd> getAllInfo(){
        return cusOrdService.getAllDetails();
    }
}
