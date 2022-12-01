package com.salesianostriana.dam.trianafy.model;


import com.fasterxml.jackson.annotation.JsonView;

import com.salesianostriana.dam.trianafy.dto.SongViews;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Song {

    @Id
    @GeneratedValue
    @JsonView(SongViews.Normal.class)
    private Long id;

    @JsonView({SongViews.Normal.class, SongViews.Created.class, SongViews.GetDto.class})
    private String title;


    @JsonView({SongViews.Normal.class, SongViews.Created.class, SongViews.GetDto.class})
    private String album;

    @Column(name = "year_of_song")
    @JsonView({SongViews.Normal.class, SongViews.Created.class, SongViews.GetDto.class})
    private String year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonView({SongViews.Normal.class, SongViews.GetDto.class})
    private Artist artist;

    @JsonView(SongViews.Created.class)
    private Long artistId;


}
