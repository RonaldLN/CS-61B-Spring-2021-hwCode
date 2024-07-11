package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    private static final int keyNum = 37;
    public static final double[] CONCERTS = new double[keyNum];
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        for (int i = 0; i < keyNum; i++) {
            CONCERTS[i] = 440.0 * Math.pow(2, (i - 24.0) / 12.0);
        }

        /* create two guitar strings, for 37 concerts */
        GuitarString[] strings = new GuitarString[keyNum];
        for (int i = 0; i < keyNum; i++) {
            strings[i] = new GuitarString(CONCERTS[i]);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = keyboard.indexOf(key);
                if (keyIndex >= 0) {
                    strings[keyIndex].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < keyNum; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < keyNum; i++) {
                strings[i].tic();
            }
        }
    }
}

