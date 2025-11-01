package org.jramirezdfernandez.monolitonaruto.ninja;

import org.hibernate.ObjectNotFoundException;
import org.jramirezdfernandez.monolitonaruto.aldea.Aldea;
import org.jramirezdfernandez.monolitonaruto.aldea.AldeaRepository;
import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;
import org.jramirezdfernandez.monolitonaruto.jutsu.JutsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final NinjaMapper ninjaMapper;

    public NinjaController(ExportacionService exportacionService, NinjaMapper ninjaMapper) {
        this.exportacionService = exportacionService;
        this.ninjaMapper = ninjaMapper;
    }

    @GetMapping
    public List<NinjaDTO> getAllNinjas() {
        List<Ninja> ninjas = ninjaRepository.findAllWithAldea();
        return ninjas.stream().map(ninjaMapper::ninjaToNinjaDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NinjaDTO> getNinjaById(@PathVariable Long id) {

        Optional<Ninja> ninjaValidacion = ninjaRepository.findById(id);

        return ninjaValidacion.map(ninja ->  ResponseEntity.ok(ninjaMapper.ninjaToNinjaDto(ninja))).orElseGet(() -> ResponseEntity.notFound().build());

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
        Ninja savedNinja = ninjaRepository.save(ninja);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedNinja.getId()).toUri();
        return ResponseEntity.created(location).body(savedNinja);
    }

    @PatchMapping
    public ResponseEntity<Ninja> modificarNinja(@RequestBody Ninja ninja) {
        Optional<Ninja> ninjaValidacionOpt = ninjaRepository.findById(ninja.getId());

        if (ninjaValidacionOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Ninja baseNinja = ninjaValidacionOpt.get();
        baseNinja.setName(ninja.getName());
        baseNinja.setRank(ninja.getRank());
        baseNinja.setAtk(ninja.getAtk());
        baseNinja.setDef(ninja.getDef());
        baseNinja.setChakra(ninja.getChakra());

        Ninja updatedNinja = ninjaRepository.save(baseNinja);
        return ResponseEntity.ok(updatedNinja);
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

        if (ninja.getJutsus().contains(jutsu) ){
            return ResponseEntity.status(409).body(ninja);
        }

        List<Jutsu> listaJutsus = ninja.getJutsus();
        if (listaJutsus == null) {
            listaJutsus = new ArrayList<>();
        }
        listaJutsus.add(jutsu);
        ninja.setJutsus(listaJutsus);

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