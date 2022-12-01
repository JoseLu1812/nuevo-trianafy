package com.salesianostriana.dam.trianafy.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.salesianostriana.dam.trianafy.model.Song;

import java.io.IOException;

public class JacksonSong {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        Song song = Song.builder().build();

        try{

            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            String normalView = mapper.writerWithView(SongViews.Normal.class).writeValueAsString(song);

            System.out.format("Vista Normal ", normalView);

            String createdView = mapper.writerWithView(SongViews.Created.class).writeValueAsString(song);

            System.out.format("Vista Created ", createdView);

            String getDtoView = mapper.writerWithView(SongViews.GetDto.class).writeValueAsString(song);

            System.out.format("Vista GetDto ", getDtoView);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
