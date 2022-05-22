import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
    private AudioInputStream audioIn;
	private SourceDataLine sourceDataLine;
    //src Path
    private File file;
    //whether loop
    private volatile boolean isLoop = false;
    //whether playing
    private volatile boolean isPlaying;
    //control the volumn
    private float newVolumn = 7;

    private PlayThread playThread;

    public MusicPlayer(String srcPath) {
        file = new File(srcPath);
    }

    //play music
    public void play() {
        if (playThread == null) {
            playThread = new PlayThread();
            playThread.start();
        }
    }

    //end music
    public void over() {
        isPlaying = false;
        if (playThread != null) {
            playThread = null;
        }
    }

    //set boolean of whether loop
    public MusicPlayer setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        return this;
    }

    //set volumn
    public MusicPlayer setVolumn(float newVolumn) {
        this.newVolumn = newVolumn;
        return this;
    }

    private class PlayThread extends Thread {

        @Override
        public void run() {
            isPlaying = true;
            do {
				isPlaying = true;

                SourceDataLine sourceDataLine = null;
                BufferedInputStream bufIn = null;
                AudioInputStream audioIn = null;
                try {
                    bufIn = new BufferedInputStream(new FileInputStream(file));
                    audioIn = AudioSystem.getAudioInputStream(bufIn);

                    AudioFormat format = audioIn.getFormat();
                    sourceDataLine = AudioSystem.getSourceDataLine(format);
                    sourceDataLine.open();
                    //must after open
                    if (newVolumn != 7) {
                        FloatControl control = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                        control.setValue(newVolumn);
                    }
                    sourceDataLine.start();
                    byte[] buf = new byte[512];
                    int len = -1;
                    while (isPlaying && (len = audioIn.read(buf)) != -1) {
                        sourceDataLine.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if (sourceDataLine != null) {
                        sourceDataLine.drain();
                        sourceDataLine.close();
                    }
                    try {
                        if (bufIn != null) {
                            bufIn.close();
                        }
                        if (audioIn != null) {
                            audioIn.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } while (isPlaying && isLoop);
        }
    }
}