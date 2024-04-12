import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.jtransforms.fft.FloatFFT_1D;

import javax.sound.sampled.*;

public class MusicVisualizer {
    AudioInputStream audioInputStream;
    File file;
    AudioFormat format;
    int channels;
    int sampleSize;
    float[] spectrum;

    public MusicVisualizer(String filename) {
        try {
            file = new File(filename);
            audioInputStream = AudioSystem.getAudioInputStream(file);
            byte[] buffer = new byte[audioInputStream.available()];
            int numBytesRead = audioInputStream.read(buffer);
            format = audioInputStream.getFormat();
            channels = format.getChannels();
            sampleSize = format.getSampleSizeInBits(); 

            if (sampleSize == 8) {
                byte[] samples = new byte[buffer.length];
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = buffer[i];
                }
            } else if (sampleSize == 16) {
                short[] samples = new short[buffer.length / 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = byteBuffer.getShort();
                }
            } else if (sampleSize == 24) {
                int[] samples = new int[buffer.length / 3];
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = ((buffer[i*3 + 2] & 0xFF) << 16) | ((buffer[i*3 + 1] & 0xFF) << 8) | (buffer[i*3] & 0xFF);
                }
            } else {
                throw new IllegalArgumentException("Unsupported sample size: " + sampleSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateSpectrum() {
        FloatFFT_1D fft = new FloatFFT_1D(spectrum.length);
    }
    public String toString() {
        return "File: " + file + "\n" +
               "Format: " + format + "\n" +
               "Channels: " + channels + "\n" +
               "Sample Size: " + sampleSize + "\n";
    }
}
