package org.jramirezdfernandez.monolitonaruto.ninja;


import org.jramirezdfernandez.monolitonaruto.aldea.AldeaRepository;
import org.jramirezdfernandez.monolitonaruto.jutsus.Jutsu;
import org.jramirezdfernandez.monolitonaruto.jutsus.JutsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ninjas")
public class NinjaController {

    @Autowired
    private NinjaRepository ninjaRepository;

    @Autowired
    private JutsuRepository jutsuRepository;

    @Autowired
    private AldeaRepository  aldeaRepository;

    @GetMapping
    public List<Ninja> getAllNinjas() {
        return ninjaRepository.findAll();
    }
    @GetMapping("/{id}")
    public Ninja getNinjaById(@PathVariable Long id) {
        return ninjaRepository.findById(id).get();
    }

    @PostMapping
    public Ninja createNinja(@RequestBody Ninja ninja) {
        return ninjaRepository.save(ninja);
    }

    @PutMapping
    public Ninja modificarNinja(@RequestBody Ninja ninja) {
        return ninjaRepository.save(ninja);
    }

    @PutMapping("/conectarnj/{id_ninja}/{id_jutsu}")
    public Ninja conectarJutsu(@PathVariable Long id_ninja, @PathVariable Long id_jutsu) {

        Ninja ninja =  ninjaRepository.findById(id_ninja).get();
        Jutsu jutsu =  jutsuRepository.findById(id_jutsu).get();
        if (ninja.getJutsus()==null) {
            ninja.setJutsus((List<Jutsu>) jutsu);
            return ninjaRepository.save(ninja);
        }

        List<Jutsu> jutsus = ninja.getJutsus();

        jutsus.add(jutsu);

        ninja.setJutsus(jutsus);

        return ninjaRepository.save(ninja);
    }

    @PutMapping("/conectarna/{id_ninja}/{id_aldea}")
    public Ninja conectarAldea(@PathVariable Long id_ninja, @PathVariable Long id_aldea) {


        Ninja ninja =  ninjaRepository.findById(id_ninja).get();

        ninja.setAldea(aldeaRepository.findById(id_aldea).get());

        return ninjaRepository.save(ninja);
    }

    @DeleteMapping("/{id}")
    public void eliminarNinja(@PathVariable Long id) {
        ninjaRepository.deleteById(id);

    }





}
