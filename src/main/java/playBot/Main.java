package playBot;

import playBot.Models.Song;
import playBot.Services.YTServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Main {

    private static final String DEVELOPER_KEY = "AIzaSyCAJnika5WB4oIDIm9Vyjc5d6Kgcgez6MU";

    public static void main(String[] args)
            throws GeneralSecurityException, IOException {
        Scanner scanner = new Scanner(System.in);

        YTServices ytServices = new YTServices();
        Queue<Song> songQueue = new LinkedList<>();
        Song song = new Song();

        boolean isRunning = true;
        int searchNum;

        System.out.println("Welcome to playbot. Press h for the list of commands.");
        String line = scanner.nextLine();
        String[] cmnd = line.split(" ", 2);

        if (cmnd[0].charAt(0) == 's') {
            searchNum = ytServices.getSearchNum(cmnd[0]);
            String query = cmnd[1];
            song = ytServices.searchSong(query, searchNum);

            System.out.println("Waiting for command...");
            line = scanner.nextLine();
            cmnd = line.split(" ", 2);

        }
        if (cmnd[0].charAt(0) == 'p') {
            char playing = 'p';

            while (isRunning) {
                if (playing == 'p') {
                    searchNum = ytServices.getSearchNum(cmnd[0]);

                    String query = cmnd[1];
                    song = ytServices.searchSong(query, searchNum);

                    songQueue = ytServices.addToQueue(songQueue, song);
                    songQueue = ytServices.playSong(songQueue);

                    System.out.println("Waiting for command...");
                    line = scanner.nextLine();
                    cmnd = line.split(" ", 2);

                    while (cmnd[0].equals("/p")) {
                        ytServices.pauseSong();
                        System.out.println("Enter /r to unpause");
                        scanner.nextLine();
                    }

                    playing = cmnd[0].charAt(0);
                } else if (playing == 'a') {
                    searchNum = ytServices.getSearchNum(cmnd[0]);
                    String query = cmnd[1];
                    song = ytServices.searchSong(query, searchNum);
                    songQueue = ytServices.addToQueue(songQueue, song);
                    while (ytServices.checkIfRunning()) {

                    }
                    songQueue = ytServices.playSong(songQueue);
                    System.out.println("Waiting for command...");
                    line = scanner.nextLine();
                    cmnd = line.split(" ", 2);
                    playing = cmnd[0].charAt(0);;
                } else if (playing == 's') {
                    if (ytServices.checkIfRunning()) {
                        Process process = Runtime.getRuntime().exec("dbus-send --type=method_call --print-reply --dest=org.mpris.MediaPlayer2.vlc /org/mpris/MediaPlayer2   org.mpris.MediaPlayer2.Quit");
                    }
                    searchNum = ytServices.getSearchNum(cmnd[0]);
                    String query = cmnd[1];
                    song = ytServices.searchSong(query, searchNum);
                    songQueue = ytServices.addToQueue(songQueue, song);
                    songQueue = ytServices.playSong(songQueue);
                    System.out.println("Waiting for command...");
                    line = scanner.nextLine();
                    cmnd = line.split(" ", 2);
                    playing = cmnd[0].charAt(0);
                } else {
                    System.out.println("Oops! Invalid command!");
                    isRunning = false;
                }
            }
        } else if (cmnd[0].charAt(0) == 'l') {
            System.out.println("Add songs. Enter s to stop.");
            line = scanner.nextLine();
            cmnd = line.split(" ", 2);
            System.out.println(cmnd[0]);
            System.out.println(cmnd[1]);
            char add = cmnd[0].charAt(0);

            while (add == 'a') {
                String query = cmnd[1];
                searchNum = ytServices.getSearchNum(cmnd[0]);
                song = ytServices.searchSong(query, searchNum);
                songQueue = ytServices.addToQueue(songQueue, song);
                System.out.println("Waiting for command...");
                line = scanner.nextLine();
                cmnd = line.split(" ", 2);
                add = cmnd[0].charAt(0);
            }

            while (!songQueue.isEmpty()) {
                songQueue = ytServices.playSong(songQueue);
                while (ytServices.checkIfRunning()) {

                }
            }
        } else if (cmnd[0].charAt(0) == 'x') {
            System.exit(0);
        }


        main(null);
    }
}
