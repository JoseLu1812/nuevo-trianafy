package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Artist", description = "El controlador de Artist")
public class ArtistController {

    private final ArtistRepository repository;
    private final SongRepository songRepository;


    @Operation(summary = "Agrega un nuevo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Artista creado correctamente",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “name”: “The name” 
                                            }                                         
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Artista no creado",
                    content = @Content),
    })
    @PostMapping("/artist")
    public ResponseEntity<Artist> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto para crear un Artista") @RequestBody Artist art){

        if(art.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(art));
    }



    @Operation(summary = "Obtiene todos los Artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han obtenido todos los artistas.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                { 
                                                    “id”: 1, 
                                                    “name”: “The name” 
                                                },
                                                { 
                                                    “id”: 2, 
                                                    “name”: “The name2” 
                                                }...
                                            ]                                        
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se han econtrado Artistas",
                    content = @Content),
    })
    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> findAll() {

        if(repository.findAll().isEmpty()){
            return  ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(repository.findAll());
    }


    @Operation(summary = "Obtiene solo un Artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han obtenido el artista.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                           { 
                                                “id”: 1, 
                                                “name”: “The name” 
                                            }                                    
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha econtrado el Artista",
                    content = @Content),
    })
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findById(@Parameter(description = "Variable tipo Long para poder obtener un Artista") @PathVariable Long id) {

        return ResponseEntity
                .of(repository.findById(id));
    }



    @Operation(summary = "Modifica un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Artista modificado con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “name”: “The name” 
                                            }                                     
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Artista no modificado",
                    content = @Content),
    })
    @PutMapping("/artist/{id}")
    public ResponseEntity<Optional<Artist>> edit(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto necesario para acceder a el Artista ha editar") @RequestBody Artist a,
                                                 @Parameter(description = "Variable necesaria para acceder al Artista a editar.") @PathVariable Long id){

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(repository.findById(id).map(c -> {
                    c.setName(a.getName());
                    repository.save(c);
                    return c;
                }));
    }



    @Operation(summary = "Elimina un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Artista eliminado con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class))
                    )}),
            @ApiResponse(responseCode = "204",
                    description = "Artista no modificado",
                    content = @Content),
    })
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<?> delete(@Parameter(description = "Variable necesaria para obtener el Artista ha editar") @PathVariable Long id){

        List<Song> listaCanciones = songRepository.findAll();
        Artist artist = repository.findById(id).get();

        if(repository.existsById(id)){
            for (int i = 0; i < listaCanciones.size(); i++){
                if(listaCanciones.get(i).getArtist() == artist){
                    listaCanciones.get(i).setArtist(null);
                }
            }
            repository.deleteById(id);
        }

        return ResponseEntity.noContent().build();
    }




}
