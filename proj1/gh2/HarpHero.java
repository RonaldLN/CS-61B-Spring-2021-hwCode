package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class HarpHero {
    private static final int KEY_NUM = 37;
    public static final double[] CONCERTS = new double[KEY_NUM];
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        for (int i = 0; i < KEY_NUM; i++) {
            CONCERTS[i] = 440.0 * Math.pow(2, (i - 24.0) / 12.0);
        }

        /* create two guitar strings, for 37 concerts */
        HarpString[] strings = new HarpString[KEY_NUM];
        for (int i = 0; i < KEY_NUM; i++) {
            strings[i] = new HarpString(CONCERTS[i]);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = KEYBOARD.indexOf(key);
                if (keyIndex >= 0) {
                    strings[keyIndex].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < KEY_NUM; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < KEY_NUM; i++) {
                strings[i].tic();
            }
        }
    }
}

