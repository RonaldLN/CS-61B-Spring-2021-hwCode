package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.NOTHING;
        }
    }

    private static void addHexagon(TETile[][] world, int s, TETile type, int startX, int startY) {
        int length = 3 * s - 2;

        for (int dy = 0; dy < s; dy++) {
            for (int dx = dy; dx < length - dy; dx++) {
                world[startX + dx][startY + 1 + dy] = type;
                world[startX + dx][startY - dy] = type;
            }
        }
    }

    private static void singleHexagonWorld(int s) {
        final int PADDING = 1;
        final int WIDTH = 3 * s - 2 + PADDING * 2;
        final int HEIGHT = s * 2 + PADDING * 2;

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        addHexagon(world, s, Tileset.FLOWER, PADDING, PADDING + s - 1);

        ter.renderFrame(world);
    }

    private static void tesselationOfHexagonsWorld(int s) {
        final int PADDING = 2;
        final int WIDTH = 3 * s - 2 + 4 * (2 * s - 1) + PADDING * 2;
        final int HEIGHT = s * 10 + PADDING * 2;

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int startX = PADDING;
        int startY = s * 3 - 1 + PADDING;
        int lineCount = 3;
        for (int i = 0; i < 5; i++) {
            int lineStartX = startX, lineStartY = startY;
            for (int j = 0; j < lineCount; j++) {
                addHexagon(world, s, randomTile(), lineStartX, lineStartY);
                lineStartX += 2 * s - 1;
                lineStartY -= s;
            }

            if (i < 2) {
                startY += s * 2;
                lineCount += 1;
            } else {
                startX += 2 * s - 1;
                startY += s;
                lineCount -= 1;
            }
        }

        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        // singleHexagonWorld(5);
        tesselationOfHexagonsWorld(3);
    }
}
