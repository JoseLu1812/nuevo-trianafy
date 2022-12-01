package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.GetSongDto;
import com.salesianostriana.dam.trianafy.dto.SongConverterDto;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongRepository repository;
    private final SongConverterDto songConverter;
    private final ArtistService artistService;




    @Operation(summary = "Agrega una nueva Canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Canción creada correctamente",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “title”: “The song”, 
                                                “artist”: “Artist name”, 
                                                “album” : “The album”,
                                                “year”: 2000
                                            }                                    
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Canción no creada",
                    content = @Content),
    })
    @PostMapping("/song/")
    public ResponseEntity<Song> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto necesario para crear una canción")
                                           @RequestBody CreateSongDto dto){

        if(dto.getTitle() == null){
            return ResponseEntity.badRequest().build();
        }
        if(dto.getAlbum() == null){
            return ResponseEntity.badRequest().build();
        }
        if(dto.getYear() == null){
            return ResponseEntity.badRequest().build();
        }
        if(dto.getArtisId() == null){
            return ResponseEntity.badRequest().build();
        }

        Song song = songConverter.convertToSong(dto);
        Artist artist = artistService.findById(dto.getArtisId()).orElse(null);
        song.setArtist(artist);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(song));
    }




    @Operation(summary = "Obtiene todas las Canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han obtenido todas  las Canciones.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                { 
                                                    “id”: 1, 
                                                    “title”: “The song”, 
                                                    “artist”: “Artist name”, 
                                                    “album” : “The album”,
                                                    “year”: 2000
                                                },
                                                { 
                                                    “id”: 2, 
                                                    “title”: “The song2”, 
                                                    “artist”: “Artist name”, 
                                                    “album” : “The album”,
                                                    “year”: 2001
                                                }...
                                            ]                                      
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se han econtrado Canciones",
                    content = @Content),
    })
    @GetMapping("/song")
    public ResponseEntity<List<Song>> findAll(){

        if(repository.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(repository.findAll());
    }




    @Operation(summary = "Obtiene una Canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Canción encontrada exitosamente.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “title”: “The song”, 
                                                “artist”: “Artist name”, 
                                                “album” : “The album”,
                                                “year”: 2000
                                            }         
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha ecnontrado la Canción",
                    content = @Content),
    })
    @GetMapping("/song/{id}")
    public ResponseEntity<Song> findById(@Parameter(description = "Variable tipo Long necesaria para obtener una Canción") @PathVariable Long id){

            return ResponseEntity
                    .of(repository.findById(id));

    }



    @Operation(summary = "Modifica una Canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Canción modificada con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “title”: “The song”, 
                                                “artist”: “Artist name”, 
                                                “album” : “The album”,
                                                “year”: 2000
                                            }                                        
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Canción no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Canción no modificada",
                    content = @Content)
    })
    @PutMapping("/song/{id}")
    public ResponseEntity<Optional<Song>> update(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto necesario para acceder a la Canción a editar ") @RequestBody GetSongDto dto,
                                                 @Parameter(description = "Variable tipo Long para obtener la canción a editar") @PathVariable Long id){

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Song song = songConverter.songToSongDto(dto);
        if( dto.getTitle() == null ){
            return ResponseEntity.badRequest().build();
        }
        if( dto.getAlbum() == null ){
            return ResponseEntity.badRequest().build();
        }
        if( dto.getYear() == null ){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .ok()
                .body(
                    repository.findById(id).map(s -> {
                        s.setTitle(song.getTitle());
                        s.setAlbum(song.getAlbum());
                        s.setYear(song.getYear());
                        s.setArtist(song.getArtist());
                        repository.save(s);
                        return s;
                    })
        );
    }




    @Operation(summary = "Elimina una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Canción eliminada con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class))
                    )}),
            @ApiResponse(responseCode = "204",
                    description = "Canción no encontrada",
                    content = @Content)
    })
    public ResponseEntity<?> delete(@Parameter(description = "Variable tipo long necesaria para poder obtener la Canción a eliminar.") @PathVariable Long id){

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
