package org.jramirezdfernandez.monolitonaruto.mision;

import org.hibernate.ObjectNotFoundException;
import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.jramirezdfernandez.monolitonaruto.ninja.NinjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
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
    public ResponseEntity<Mision> createMision (@RequestBody Mision mision) {
        Mision savedMision = misionRepository.save(mision); // Guarda y obtén el ID
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedMision.getId()).toUri();
        return ResponseEntity.created(location).body(savedMision); // Devuelve el objeto creado
    }

    @PatchMapping
    public ResponseEntity<Mision> modificarMision(@RequestBody Mision mision) {
        Optional<Mision> baseMisionOpt = misionRepository.findById(mision.getId());

        if (baseMisionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mision baseMision = baseMisionOpt.get();

        // Si mision.ninja no es null en el request body, significa que se está intentando cambiar el ninja.
        // Si no, se mantiene el ninja existente.
        // Aquí asumimos que el RequestBody 'mision' tiene todos los campos de la misión actualizados,
        // pero la relación con 'ninja' es lo que queremos manejar con el PATCH específico de '/{id_mision}/{id_ninja}'.
        // Si la intención es que PATCH /api/misiones solo actualice campos de la misión,
        // sin tocar al ninja, entonces el código original era casi correcto, pero devolvía el `baseMision`
        // sin los cambios del `RequestBody`.

        // Copia los campos actualizables de 'mision' del RequestBody a 'baseMision'
        baseMision.setName(mision.getName());
        baseMision.setRank(mision.getRank());
        baseMision.setRecompensa(mision.getRecompensa());
        baseMision.setRequisitorango(mision.getRequisitorango());
        // NO copies el ninja de mision (RequestBody) aquí, usa el endpoint específico para eso
        // baseMision.setNinja(mision.getNinja()); // <-- esto no debería hacerse aquí si se usa endpoint /id_mision/id_ninja

        Mision updatedMision = misionRepository.save(baseMision); // Guarda los cambios en la misión base
        return ResponseEntity.ok(updatedMision); // Devuelve el objeto actualizado
    }


    @PatchMapping("/{id_mision}/{id_ninja}")
    public ResponseEntity<Mision> conectarNinja(@PathVariable Long id_mision, @PathVariable Long id_ninja) {
        Optional<Mision> misionValidacion =  misionRepository.findById(id_mision);

        if (misionValidacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Si ya tiene un ninja, lanza un bad request
        if (misionValidacion.get().getNinja() != null){
            return ResponseEntity.badRequest().body(misionValidacion.get()); // Podrías devolver la misión existente para dar contexto.
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

        // Si la validación de rango falla
        return ResponseEntity.badRequest().body(mision); // Podrías devolver la misión sin cambios para dar contexto.
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