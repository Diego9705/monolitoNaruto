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
        Ninja savedNinja = ninjaRepository.save(ninja); // Guarda y obtén el ID
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedNinja.getId()).toUri();
        return ResponseEntity.created(location).body(savedNinja); // Devuelve el objeto creado
    }


    @PatchMapping
    public ResponseEntity<Ninja> modificarNinja(@RequestBody Ninja ninja) {
        Optional<Ninja> ninjaValidacionOpt = ninjaRepository.findById(ninja.getId());

        if (ninjaValidacionOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Ninja baseNinja = ninjaValidacionOpt.get();

        // Para un PATCH, solo queremos actualizar los campos que vienen en el RequestBody.
        // Sin embargo, si el RequestBody `ninja` no tiene `jutsus` o `aldea` se pondrían a null.
        // Para evitar esto y mantener las relaciones existentes,
        // asignamos las relaciones del ninja base al ninja del RequestBody antes de guardarlo.
        // O más limpio, actualizar campo por campo en el `baseNinja`.

        // Opción 1: Actualizar campos específicos del baseNinja
        baseNinja.setName(ninja.getName());
        baseNinja.setRank(ninja.getRank());
        baseNinja.setAtk(ninja.getAtk());
        baseNinja.setDef(ninja.getDef());
        baseNinja.setChakra(ninja.getChakra());
        // NO tocar las colecciones o relaciones OneToOne/ManyToOne aquí si tienen endpoints específicos
        // baseNinja.setJutsus(ninja.getJutsus()); // <--- esto se maneja con conectarnj
        // baseNinja.setAldea(ninja.getAldea()); // <--- esto se maneja con conectarna

        Ninja updatedNinja = ninjaRepository.save(baseNinja); // Guarda el objeto 'baseNinja' con los cambios
        return ResponseEntity.ok(updatedNinja); // Devuelve el objeto actualizado
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

        // Verificar si el ninja ya tiene el jutsu
        if (ninja.getJutsus().contains(jutsu) ){
            // Devuelve un 409 Conflict o 422 Unprocessable Entity
            return ResponseEntity.status(409).body(ninja); // Devuelve el ninja para dar contexto
        }

        List<Jutsu> listaJutsus = ninja.getJutsus();
        if (listaJutsus == null) { // Inicializa si es null
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