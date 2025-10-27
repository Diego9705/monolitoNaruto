package org.jramirezdfernandez.monolitonaruto.ninja;


import org.jramirezdfernandez.monolitonaruto.aldea.AldeaRepository;
import org.jramirezdfernandez.monolitonaruto.exportacion.ExportacionService;
import org.jramirezdfernandez.monolitonaruto.exportacion.FormatoJSON;
import org.jramirezdfernandez.monolitonaruto.exportacion.VisitorFormato;
import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;
import org.jramirezdfernandez.monolitonaruto.jutsu.JutsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    private final ExportacionService exportacionService;

    public NinjaController(ExportacionService exportacionService) {
        this.exportacionService = exportacionService;
    }


    @GetMapping
    public List<Ninja> getAllNinjas() {
        return ninjaRepository.findAll();
    }
    @GetMapping("/{id}")
    public Ninja getNinjaById(@PathVariable Long id) {
        return ninjaRepository.findById(id).get();
    }


    @GetMapping("/{id_ninja}/{opcion}")
    public ResponseEntity<byte[]> exportarNinja(@PathVariable Long id_ninja,@PathVariable Integer opcion) throws IOException {
        Ninja ninja = ninjaRepository.findById(id_ninja).get();

        ArrayList<Object> propiedades = exportacionService.obtenerPropiedadesExportar(opcion);

        byte[] fileContent = ninja.aceptarExportarFormato((VisitorFormato) propiedades.get(0));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, (String) propiedades.get(1))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ninja.getName() + propiedades.get(2) + "\"")
                .body(fileContent);
    }


    @PostMapping
    public Ninja createNinja(@RequestBody Ninja ninja) {
        return ninjaRepository.save(ninja);
    }


    @PatchMapping
    public Ninja modificarNinja(@RequestBody Ninja ninja) {

        Ninja baseNinja = ninjaRepository.findById(ninja.getId()).get();

        ninja.setJutsus(baseNinja.getJutsus());
        return ninjaRepository.save(ninja);
    }

    @PatchMapping("/conectarnj/{id_ninja}/{id_jutsu}")
    public Ninja conectarJutsu(@PathVariable Long id_ninja, @PathVariable Long id_jutsu) {

        Ninja ninja =  ninjaRepository.findById(id_ninja).get();
        Jutsu jutsu =  jutsuRepository.findById(id_jutsu).get();

        if (ninja.getJutsus()==null) {
            List<Jutsu> lista = new ArrayList<>();
            lista.add(jutsu);
            ninja.setJutsus(lista);
            return ninjaRepository.save(ninja);
        }

        List<Jutsu> jutsus = ninja.getJutsus();

        jutsus.add(jutsu);

        ninja.setJutsus(jutsus);

        return ninjaRepository.save(ninja);
    }

    @PatchMapping("/conectarna/{id_ninja}/{id_aldea}")
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
