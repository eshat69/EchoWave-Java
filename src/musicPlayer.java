import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.FloatControl;


public class musicPlayer {
    static String[] playlist = {
            "src/joybangla.wav",
            "src/Bulbuli.wav",
            "src/PRETTY_LITTLE_BABY.wav",
            "src/Sapphire.wav",
            "src/Cupid_Twins.wav",

    };

    static int currentIndex = 0;
    static Clip clip;


    public static void main(String[] args){
        System.out.println("Welcome to EchoWave-Java");

        try (Scanner scanner = new Scanner(System.in)) {
            loadAndPlay(currentIndex);

            String response = "";
            while (!response.equals("q")) {
                System.out.println("\np = play");
                System.out.println("s = stop");
                System.out.println("r = reset");
                System.out.println("n = next music");
                System.out.println("b = previous music");
                System.out.println("+ = volume up");
                System.out.println("- = volume down");
                System.out.println("q = quit");
                System.out.println("Enter what you want:");

                response = scanner.next().toLowerCase();
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                switch (response) {
                    case "p" -> clip.start();
                    case "s" -> clip.stop();
                    case "r" -> clip.setMicrosecondPosition(0);
                    case "q" -> {
                        clip.close();
                        System.out.println("Goodbye!");
                    }
                    case "n" -> {
                        currentIndex = (currentIndex + 1) % playlist.length;
                        System.out.println("Next: " + playlist[currentIndex]);
                        loadAndPlay(currentIndex);
                    }
                    case "b" -> {
                        currentIndex = (currentIndex - 1 + playlist.length) % playlist.length;
                        System.out.println("Previous: " + playlist[currentIndex]);
                        loadAndPlay(currentIndex);
                    }
                    case "+" -> {
                        float current = volumeControl.getValue();
                        volumeControl.setValue(Math.min(current + 5.0f, volumeControl.getMaximum()));
                        System.out.println("Volume increased");
                    }
                    case "-" -> {
                        float current = volumeControl.getValue();
                        volumeControl.setValue(Math.max(current - 5.0f, volumeControl.getMinimum()));
                        System.out.println("Volume decreased");
                    }
                    default -> System.out.println("Invalid choice");
                }
            }

        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }

    // Loads a new clip from playlist index and auto-plays it
    static void loadAndPlay(int index) {
        try {
            // Close previous clip if open
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            File file = new File(playlist[index]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            System.out.println("Now playing: " + playlist[index]);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio format: " + playlist[index]);
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
        } catch (IOException e) {
            System.out.println("File not found or IO error: " + playlist[index]);
        }
    }
}

