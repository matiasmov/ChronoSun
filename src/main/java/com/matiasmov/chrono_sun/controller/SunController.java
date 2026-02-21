package com.matiasmov.chrono_sun.controller;

import com.matiasmov.chrono_sun.SunService;
import com.matiasmov.chrono_sun.dto.SunDataResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sun")
@CrossOrigin(origins = "*") 
public class SunController {

 
    private final SunService sunService;

 
    public SunController(SunService sunService) {
        this.sunService = sunService;
    }

    @GetMapping("/search")
    public SunDataResponse search(@RequestParam String address) {
     
        return sunService.getSunDataByAddress(address);
    }
}