package com.udemy.s19.music.db;

import com.udemy.s19.music.db.model.Artist;
import com.udemy.s19.music.db.model.Datasource;
import com.udemy.s19.music.db.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        // the result sorted by artist name.
        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_NONE);
        if (artists == null) {
            System.out.println("No artists!");
            return;
        }
        for (Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        //the result for specified artist - sorted albums
        List<String> albumsForArtist = datasource.queryAlbumsForArtist("Carole King", Datasource.ORDER_BY_ASC);

        for (String album : albumsForArtist){
            System.out.println(album);
        }

        //the result for specified song returning artist name, album and track
        List<SongArtist> songArtists = datasource.queryArtistForSong("Go Your Own Way", Datasource.ORDER_BY_ASC);
        if(songArtists == null){
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist: songArtists) {
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        //We can get information such as the column names and types and attributes from table
        datasource.querySongsMetadata();

        //count method on table songs
        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is: " + count);

        // create the view of the song artist
        datasource.createViewForSongArtists();

        //the result for specified song returning artist name, album and track from view using user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song title: ");
        String title = scanner.nextLine();

        songArtists = datasource.querySongInfoView(title);

        //the result for specified song returning artist name, album and track from view
        //songArtists = datasource.querySongInfoView("She's On Fire");
        if(songArtists.isEmpty()){
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist: songArtists) {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track number = " + artist.getTrack());
        }

        //inserting song
        datasource.insertSong("Bird Dog", "Everly Brothers", "All-Time Greatest Hits", 7);

        datasource.close();
    }
}