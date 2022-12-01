package com.salesianostriana.dam.trianafy.controller;


import com.salesianostriana.dam.trianafy.dto.CreatePlaylistDto;
import com.salesianostriana.dam.trianafy.dto.GetPlaylistDto;
import com.salesianostriana.dam.trianafy.dto.PlaylistConverterDto;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
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
public class PlaylistController {

    private PlaylistRepository repository;
    private PlaylistConverterDto playlistConverterDto;
    private SongRepository songRepository;


    @Operation(summary = "Agrega una nueva Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Playlist creada correctamente",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            { 
                                                “id”: 1, 
                                                “name”: “The name”, 
                                                “description”: “The desc” 
                                            }                                    
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Playlist no creada",
                    content = @Content),
    })
    @PostMapping("/list/")
    public ResponseEntity<Playlist> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto necesario de para crear la Playlist") @RequestBody CreatePlaylistDto dto){

        if(dto.getName() == null){
           return ResponseEntity.badRequest().build();
        }
        if(dto.getDescription() == null){
            return ResponseEntity.badRequest().build();
        }

        Playlist list = playlistConverterDto.convertToPlaylist(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(list));
    }




    @Operation(summary = "Obtiene todas las Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han obtenido todas  las Playlist.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                { 
                                                    “id”: 1, 
                                                    “name”: “The name”, 
                                                    “numberOfSongs”: 7 
                                                },
                                                {
                                                  “id”: 2, 
                                                  “name”: “The name2”, 
                                                  “numberOfSongs”: 8  
                                                }…
                                            ]                                   
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha econtrado ninguna Playlist",
                    content = @Content),
    })
    @GetMapping("/list/")
    public ResponseEntity<List<GetPlaylistDto>> findAll(){

        if(repository.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<GetPlaylistDto> lista = playlistConverterDto.getAllPlaylist(repository.findAll());

        return ResponseEntity
                .ok()
                .body(lista);
    }



    @Operation(summary = "Obtiene una Playlist concreta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Playlist encontrada exitosamente.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            
                                            {
                                               “id”: 1,
                                               “name”: “The name”,
                                               “description”: “The desc”
                                               “songs”: 
                                                    [
                                                         { “id”: 1, “title”: “The song”,
                                                           “artist”: “Artist name”,
                                                           “album” : “The album”, “year”: 2000},
                                                         { “id”: 2, “title”: “Another song”,
                                                           “artist”: “Another Artist name”,
                                                           “album” : “Another album”, “year”: 2020},
                                                        ...
                                                    ]
                                            }
                                                    
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la Playlist solicitada",
                    content = @Content),
    })
    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findById(@Parameter(description = "Variable tipo Long necesaria para obtener la Playlist") @PathVariable Long id){
        if(repository.findById(id).isPresent()){
            return ResponseEntity
                    .ok()
                    .body(repository.findById(id).get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }




    @Operation(summary = "Modifica una Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "PlaylistCanción modificada con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
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
                    description = "Playlist no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Playlist no modificada",
                    content = @Content)
    })@PutMapping("/list/{id}")
    public ResponseEntity<GetPlaylistDto> edit(@RequestBody CreatePlaylistDto dto, @PathVariable Long id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        Playlist list = repository.findById(id)
                .map(p -> {
                    p.setDescription(dto.getDescription());
                    p.setName(dto.getName());
                    return repository.save(p);
                }).orElse(null);

        if(list == null){
            return ResponseEntity.notFound().build();
        }

        GetPlaylistDto playlistDto = playlistConverterDto.playlistToGetPlaylistDto(list);

        return ResponseEntity
                .ok(playlistDto);

    }



    @Operation(summary = "Elimina una Playlist al completo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Playlist eliminada con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class))
                    )}),
            @ApiResponse(responseCode = "204",
                    description = "Playlist no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/list/{id}")
    public ResponseEntity<?> delete(@Parameter(description = "Variable necesaria para obtener la Playlist a eliminar") @PathVariable Long id){

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id).map(l -> {
            l.setName(null);
            l.setSongs(null);
            l.setDescription(null);
            repository.save(l);
            return l;
        });

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }





    @Operation(summary = "Agrega una Canción a una Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Canción agregada correctamente  a la Playlist",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                               “id”: 1,
                                               “name”: “The name”,
                                               “description”: “The desc”
                                               “songs”: [
                                                 { 
                                                    “id”: 1, 
                                                    “title”: “The song”,
                                                    “artist”: “Artist name”,
                                                    “album” : “The album”,
                                                    “year”: 2000
                                                 },
                                                 { 
                                                    “id”: 2, 
                                                    “title”: “Another song”,
                                                    “artist”: “Another Artist name”,
                                                    “album” : “Another album”, 
                                                    “year”: 2020
                                                 },
                                                ...
                                               ]
                                            }
                                                                              
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Playlist/Canción no encontrada",
                    content = @Content),
    })
    @PostMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Optional<Playlist>> addSongToPlaylist(@Parameter(description = "Varibale tipo Long necesaria para acceder a la Playlist")@PathVariable Long id1,
                                                                @Parameter(description = "variable tipo Long necesaria para obtener la canción solicitada") @PathVariable Long id2){
        if(!songRepository.existsById(id1)){
            return ResponseEntity.notFound().build();
        }
        else if (!repository.existsById(id2)) {
            return ResponseEntity.notFound().build();
        }

        Optional<Playlist> playlist = repository.findById(id1);

        return ResponseEntity
                .ok()
                .body(playlist.map(l -> {
                    l.addSong(songRepository.findById(id2).get());
                    repository.save(l);
                    return l;
                })
                );
    }



    @Operation(summary = "Obtiene las Canciones de  una Playlist concreta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Playlist encontrada exitosamente.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """                          
                                            {
                                               “id”: 1,
                                               “name”: “The name”,
                                               “description”: “The desc”
                                               “songs”: [
                                                 { “id”: 1, “title”: “The song”,
                                                   “artist”: “Artist name”,
                                                   “album” : “The album”, “year”: 2000},
                                                 { “id”: 2, “title”: “Another song”,
                                                   “artist”: “Another Artist name”,
                                                   “album” : “Another album”, “year”: 2020},
                                                ...
                                               ]
                                            }  
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la Playlist solicitada",
                    content = @Content),
    })
    @GetMapping("/list/{id}/song")
    public ResponseEntity<Playlist> getPlaylistSongs(@Parameter(description = "Variable tipo long necesaria para acceder a la Playlist") @PathVariable Long id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(repository.findById(id).get());
    }





    @Operation(summary = "Obtiene una Canción de una Playlist concreta")
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
                                            },  
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la Playlist/Canción solicitada",
                    content = @Content),
    })
    @GetMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> getSongFromPlaylist(@Parameter(description = "Variable necesaria para obtener la Playlist") @PathVariable Long id1,
                                                    @Parameter(description = "Variable necesaria para obtener la Canción solicitada") @PathVariable Long id2){

        if(!repository.existsById(id1)){
            return ResponseEntity.notFound().build();
        } else if (!repository.existsById(id2)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(repository.findById(id1).get().getSongs().get(id2.intValue()));
    }



    @Operation(summary = "Elimina una Canción de una Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Canción eliminada con éxito.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class))
                    )}),
            @ApiResponse(responseCode = "204",
                    description = "Playlist/Canción no encontrada",
                    content = @Content)
    })@DeleteMapping("/list/{id1}/song/{id2}")
     public ResponseEntity<?> deleteSongFromPlaylist(@Parameter(description = "Variable tipo Long necesario para obtener una Playlist") @PathVariable Long id1,
                                                     @Parameter(description = "Variable tipo Long necesario par obtener la Canción a eliminar") @PathVariable Long id2){
        if(!repository.existsById(id1)){
            return ResponseEntity.notFound().build();
        } else if (!repository.existsById(id2)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(repository.findById(id1).get().getSongs().remove(id2));
    }



}
