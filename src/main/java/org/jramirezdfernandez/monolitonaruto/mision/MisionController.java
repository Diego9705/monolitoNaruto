package org.jramirezdfernandez.monolitonaruto.mision;

import org.hibernate.ObjectNotFoundException;
import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.jramirezdfernandez.monolitonaruto.ninja.NinjaRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@RequestMapping("/api/misiones")
public class MisionController {

    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private NinjaRepository ninjaRepository;


    private final MisionMapper misionMapper;

    private final MisionService misionService;

    private final ExportacionService exportacionService;

    public MisionController(MisionMapper misionMapper, MisionService misionService, ExportacionService exportacionService) {
        this.misionMapper = misionMapper;
        this.misionService = misionService;
        this.exportacionService = exportacionService;
    }

    @GetMapping
    public List<MisionDTO> getAllMisiones() {
        List<Mision> misiones = misionRepository.findAll();
        return misiones.stream().map(misionMapper::misionToMisionDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<MisionDTO> getMisionById(@PathVariable Long id) {

        Optional<Mision> opt = misionRepository.findById(id);

        return opt.map(mision -> ResponseEntity.ok(misionMapper.misionToMisionDTO(mision))).orElseGet(() -> ResponseEntity.notFound().build());
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
    public ResponseEntity<Mision> createMision (@RequestBody Mision mision) {

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(mision.getId()).toUri();

        misionRepository.save(mision);

        return ResponseEntity.created(location).build();
    }

    @PatchMapping
    public ResponseEntity<Mision> modificarMision(@RequestBody Mision mision) {

        Optional<Mision> baseMision = misionRepository.findById(mision.getId());

        if (baseMision.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mision.setNinja(baseMision.get().getNinja());
        misionRepository.save(mision);

        return ResponseEntity.ok(mision);

    }


    @PatchMapping("/{id_mision}/{id_ninja}")
    public ResponseEntity<Mision> conectarNinja(@PathVariable Long id_mision, @PathVariable Long id_ninja) {


        Optional<Mision> misionValidacion =  misionRepository.findById(id_mision);

        if (misionValidacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        if (misionValidacion.get().getNinja() != null){
            return ResponseEntity.badRequest().build();
        }

        Optional<Ninja> ninjaValidacion =  ninjaRepository.findById(id_ninja);

        if (ninjaValidacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mision mision = misionValidacion.get();
        Ninja ninja = ninjaValidacion.get();

        boolean validacion = misionService.validarRango(mision.getRequisitorango(),ninja.getRank());


        if (validacion){
            mision.setNinja(ninja);
            misionRepository.save(mision);
            return ResponseEntity.ok(mision);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMision(@PathVariable Long id) {
        try{
            misionRepository.deleteById(id);
            return  ResponseEntity.noContent().build();

        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
