package org.jramirezdfernandez.monolitonaruto.ninja;


import org.hibernate.ObjectNotFoundException;
import org.jramirezdfernandez.monolitonaruto.aldea.Aldea;
import org.jramirezdfernandez.monolitonaruto.aldea.AldeaRepository;
import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.exportacion.FormatoJSON;
import org.jramirezdfernandez.monolitonaruto.exportacion.VisitorFormato;
import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;
import org.jramirezdfernandez.monolitonaruto.jutsu.JutsuRepository;
import org.jramirezdfernandez.monolitonaruto.mision.Mision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.System.in;

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

    private final ExportacionService exportacionService;

    public NinjaController(ExportacionService exportacionService) {
        this.exportacionService = exportacionService;
    }


    @GetMapping
    public List<Ninja> getAllNinjas() {
        return ninjaRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Ninja> getNinjaById(@PathVariable Long id) {

        Optional<Ninja> ninjaValidacion = ninjaRepository.findById(id);

        return ninjaValidacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }


    @GetMapping("/{id_ninja}/{opcion}")
    public ResponseEntity<byte[]> exportarNinja(@PathVariable Long id_ninja,@PathVariable Integer opcion) throws IOException {
        Optional<Ninja> ninjaVerificar = ninjaRepository.findById(id_ninja);

        if (ninjaVerificar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ninja ninja = ninjaVerificar.get();

        return exportacionService.exportar(ninja,opcion);
    }


    @PostMapping
    public ResponseEntity<Ninja> createNinja(@RequestBody Ninja ninja) {

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ninja.getId()).toUri();

        ninjaRepository.save(ninja);

        return ResponseEntity.created(location).build();

    }


    @PatchMapping
    public ResponseEntity<Ninja> modificarNinja(@RequestBody Ninja ninja) {

        Optional<Ninja> ninjaValidacion = ninjaRepository.findById(ninja.getId());

        if (ninjaValidacion.isEmpty()){
            return ResponseEntity.notFound().build();
        }


        Ninja baseNinja = ninjaValidacion.get();

        ninja.setJutsus(baseNinja.getJutsus());
        ninjaRepository.save(ninja);

        return ResponseEntity.ok(baseNinja);
    }

    @PatchMapping("/conectarnj/{id_ninja}/{id_jutsu}")
    public ResponseEntity<Ninja> conectarJutsu(@PathVariable Long id_ninja, @PathVariable Long id_jutsu) {

        Optional<Ninja> ninjaValidacion = ninjaRepository.findById(id_ninja);

        if (ninjaValidacion.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Optional<Jutsu> jutsuValidacion = jutsuRepository.findById(id_jutsu);

        if (jutsuValidacion.isEmpty()){
            return ResponseEntity.notFound().build();
        }


        Ninja ninja =  ninjaValidacion.get();

        Jutsu jutsu =  jutsuValidacion.get();

        List<Jutsu> lista;

        if (ninja.getJutsus().contains(jutsu) ){
            return ResponseEntity.unprocessableEntity().build();
        }

        if (ninja.getJutsus().isEmpty()) {
            lista = new ArrayList<>();
            lista.add(jutsu);
        }
        else {
            lista = ninja.getJutsus();
            lista.add(jutsu);
        }

        ninja.setJutsus(lista);

        ninjaRepository.save(ninja);

        return ResponseEntity.ok(ninja);
    }

    @PatchMapping("/conectarna/{id_ninja}/{id_aldea}")
    public ResponseEntity<Ninja> conectarAldea(@PathVariable Long id_ninja, @PathVariable Long id_aldea) {

        Optional<Ninja> ninjaValidacion = ninjaRepository.findById(id_ninja);

        if (ninjaValidacion.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Optional<Aldea> aldeaValidacion = aldeaRepository.findById(id_aldea);

        if (aldeaValidacion.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Ninja ninja =  ninjaValidacion.get();

        ninja.setAldea(aldeaValidacion.get());

        ninjaRepository.save(ninja);

        return ResponseEntity.ok(ninja);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNinja(@PathVariable Long id) {

        try{
            ninjaRepository.deleteById(id);
            return  ResponseEntity.noContent().build();

        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
