package org.example.defynske.controller;


import lombok.RequiredArgsConstructor;
import org.example.defynske.model.Wine;
import org.example.defynske.repo.WineRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WineController {

    private final WineRepo wineRepo;

    @GetMapping
    public List<Wine> getAll(){
        return wineRepo.findAll();
    }

    @PostMapping
    public Wine add(@RequestBody Wine wine){
        return wineRepo.save(wine);
    }
}
