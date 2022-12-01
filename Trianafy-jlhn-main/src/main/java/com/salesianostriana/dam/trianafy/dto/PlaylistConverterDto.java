package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaylistConverterDto {

    private GetPlaylistDto getPlaylistDto;

    public Playlist convertToPlaylist(CreatePlaylistDto p){
        return Playlist.builder()
                .name(p.getName())
                .description(p.getDescription())
                .build();
    }

    public List<GetPlaylistDto> getAllPlaylist(List<Playlist> list){

        List<GetPlaylistDto> newList = new ArrayList<GetPlaylistDto>();

        for(int i = 0;i<list.size();i++){
            newList.add(GetPlaylistDto.builder()
                    .id(list.get(i).getId())
                    .name(list.get(i).getName())
                    .numberOfSongs(list.get(i).getSongs().size())
                    .build());
        }

        return newList;
    }

    public GetPlaylistDto playlistToGetPlaylistDto(Playlist p){

        return GetPlaylistDto
                .builder()
                .id(p.getId())
                .name(p.getName())
                .numberOfSongs(p.getSongs().size())
                .build();
    }


}


