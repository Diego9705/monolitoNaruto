package org.jramirezdfernandez.monolitonaruto.aldea;


import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Aldea> getAldeaById(@PathVariable Long id) {
        Optional<Aldea> opt = aldeaRepository.findById(id);

        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/predeterminados")
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
