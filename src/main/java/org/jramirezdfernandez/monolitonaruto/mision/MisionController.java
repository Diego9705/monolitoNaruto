package org.jramirezdfernandez.monolitonaruto.mision;

import org.jramirezdfernandez.monolitonaruto.jutsus.Jutsu;
import org.jramirezdfernandez.monolitonaruto.jutsus.JutsuRepository;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.jramirezdfernandez.monolitonaruto.ninja.NinjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/misiones")
public class MisionController {




    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private NinjaRepository ninjaRepository;


    private final MisionService misionService;

    public MisionController(MisionService misionService) {
        this.misionService = misionService;
    }

    @GetMapping
    public List<Mision> getAllMisiones() {
        return misionRepository.findAll();
    }
    @GetMapping("/{id}")
    public Mision getMisionById(@PathVariable Long id) {
        return misionRepository.findById(id).get();
    }

    @GetMapping("/crear/predeterminados")
    public void misionesPredeterminados() {
        misionRepository.save(Mision.builder().name("Exploración").rank("D").recompensa(10).requisitorango("Genin").build());
        misionRepository.save(Mision.builder().name("Recopilación de datos").rank("C").recompensa(15).requisitorango("Genin").build());
        misionRepository.save(Mision.builder().name("Escolta").rank("B").recompensa(20).requisitorango("Chunin").build());
        misionRepository.save(Mision.builder().name("Rescate").rank("A").recompensa(25).requisitorango("Chunin").build());
        misionRepository.save(Mision.builder().name("Batalla").rank("S").recompensa(30).requisitorango("Jonin").build());
    }

    @PostMapping
    public Mision createMision (@RequestBody Mision mision) {
        return misionRepository.save(mision);
    }

    @PutMapping("/conectarmn/{id_mision}/{id_ninja}")
    public ResponseEntity<Mision> conectarNinja(@PathVariable Long id_mision, @PathVariable Long id_ninja) {


        Mision mision =  misionRepository.findById(id_mision).get();
        Ninja ninja =  ninjaRepository.findById(id_ninja).get();

        boolean validacion = misionService.validarRango(mision.getRequisitorango(),ninja.getRank());


        if (validacion){
            mision.setNinja(ninja);
            misionRepository.save(mision);
            return ResponseEntity.ok(mision);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public void eliminarMision(@PathVariable Long id) {
        ninjaRepository.deleteById(id);
    }

}
