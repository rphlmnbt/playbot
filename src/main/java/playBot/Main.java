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
        Song song = ytServices.searchSong();
        Queue<Song> songQueue = new LinkedList<>();
        boolean isRunning = true;
        char playing = 'p';

        while (isRunning) {
            if (playing == 'p') {
                songQueue = ytServices.addToQueue(songQueue, song);
                songQueue = ytServices.playSong(songQueue);
                System.out.println("Keep playing?");
                playing = scanner.next().charAt(0);
            } else if (playing == 'q') {
                song = ytServices.searchSong();
                songQueue = ytServices.addToQueue(songQueue, song);
                while (ytServices.checkIfRunning()) {

                }
                songQueue = ytServices.playSong(songQueue);
                System.out.println("Keep playing?");
                playing = scanner.next().charAt(0);;
            } else if (playing == 's') {
                if (ytServices.checkIfRunning()) {
                    Process process = Runtime.getRuntime().exec("kill mpv");
                }
                song = ytServices.searchSong();
                songQueue = ytServices.addToQueue(songQueue, song);
                songQueue = ytServices.playSong(songQueue);
                while (ytServices.checkIfRunning()) {

                }
            } else {
                System.out.println("Oops! Invalid command!");
                isRunning = false;
            }
        }
    }







}
