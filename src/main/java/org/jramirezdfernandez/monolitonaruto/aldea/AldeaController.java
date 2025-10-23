package org.jramirezdfernandez.monolitonaruto.aldea;


import org.jramirezdfernandez.monolitonaruto.jutsus.Jutsu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/aldeas")
public class AldeaController {

    @Autowired
    private AldeaRepository aldeaRepository;

    @GetMapping
    public List<Aldea> getAllaldeas() {
        return aldeaRepository.findAll();
    }


    @GetMapping("/{id}")
    public Aldea getAldeaById(@PathVariable Long id) {
        return aldeaRepository.findById(id).get();
    }

    @GetMapping("/crear/predeterminados")
    public void aldeasPredeterminados() {
        aldeaRepository.save(Aldea.builder().name("Aldea Konoha").build());
        aldeaRepository.save(Aldea.builder().name("Aldea Kiri").build());
        aldeaRepository.save(Aldea.builder().name("Aldea Suna").build());
    }

    @PostMapping
    public Aldea createAldea(@RequestBody Aldea aldea) {
        return aldeaRepository.save(aldea);
    }
}
