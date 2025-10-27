package org.jramirezdfernandez.monolitonaruto.mision;

import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.exportacion.VisitorFormato;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.jramirezdfernandez.monolitonaruto.ninja.NinjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api/misiones")
public class MisionController {




    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private NinjaRepository ninjaRepository;


    private final MisionService misionService;

    private final ExportacionService exportacionService;

    public MisionController(MisionService misionService, ExportacionService exportacionService) {
        this.misionService = misionService;
        this.exportacionService = exportacionService;
    }

    @GetMapping
    public List<Mision> getAllMisiones() {
        return misionRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Mision> getMisionById(@PathVariable Long id) {
        Optional<Mision> opt = misionRepository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/predeterminados")
    public void misionesPredeterminados() {
        misionRepository.save(Mision.builder().name("Exploración").rank("D").recompensa(10).requisitorango("Genin").build());
        misionRepository.save(Mision.builder().name("Recopilación de datos").rank("C").recompensa(15).requisitorango("Genin").build());
        misionRepository.save(Mision.builder().name("Escolta").rank("B").recompensa(20).requisitorango("Chunin").build());
        misionRepository.save(Mision.builder().name("Rescate").rank("A").recompensa(25).requisitorango("Chunin").build());
        misionRepository.save(Mision.builder().name("Batalla").rank("S").recompensa(30).requisitorango("Jonin").build());
    }


    @GetMapping("/{id_mision}/{opcion}")
    public ResponseEntity<byte[]> exportarMision(@PathVariable Long id_mision,@PathVariable Integer opcion) throws IOException {
        Optional<Mision> misionVerificar = misionRepository.findById(id_mision);

        if (misionVerificar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mision mision = misionVerificar.get();

        return exportacionService.exportar(mision,opcion);
    }


    @PostMapping
    public Mision createMision (@RequestBody Mision mision) {
        return misionRepository.save(mision);
    }

    @PatchMapping
    public Mision modificarMision(@RequestBody Mision mision) {

        Mision baseMision = misionRepository.findById(mision.getId()).get();

        mision.setNinja(baseMision.getNinja());

        return misionRepository.save(mision);
    }




    @PatchMapping("/{id_mision}/{id_ninja}")
    public ResponseEntity<Mision> conectarNinja(@PathVariable Long id_mision, @PathVariable Long id_ninja) {


        Mision mision =  misionRepository.findById(id_mision).get();

        if (mision.getNinja() != null){
            return ResponseEntity.badRequest().build();
        }

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
