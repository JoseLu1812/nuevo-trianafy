package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongConverterDto {

    public Song convertToSong(CreateSongDto s){
        return Song.builder()
                .title(s.getTitle())
                .album(s.getAlbum())
                .year(s.getYear())
                .build();
    }

    public Song songToSongDto(GetSongDto s){
        return Song.builder()
                .title(s.getTitle())
                .album(s.getAlbum())
                .year(s.getYear())
                .artist(s.getArtist())
                .build();
    }




}
