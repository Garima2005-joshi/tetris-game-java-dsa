package project;

import java.awt.*;

public class Tetromino {

    public Point[][] shape;
    public Color color;

    public int rotation = 0;
    public int x = 4;
    public int y = 0;

    public Tetromino(int type) {
        shape = TETROMINOS[type];
        color = COLORS[type];
    }

    // you can set position/rotation externally
    // e.g. t.rotation = r; t.x = x; t.y = y;

    public static final Point[][][] TETROMINOS = {
        // I (type 0)
        {
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(2,0) },
            { new Point(0,-1), new Point(0,0), new Point(0,1), new Point(0,2) },
            { new Point(-1,1), new Point(0,1), new Point(1,1), new Point(2,1) },
            { new Point(1,-1), new Point(1,0), new Point(1,1), new Point(1,2) }
        },
        // O (type 1)
        {
            { new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1) },
            { new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1) },
            { new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1) },
            { new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1) }
        },
        // T (type 2)
        {
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(0,1) },
            { new Point(0,-1), new Point(0,0), new Point(1,0), new Point(0,1) },
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(0,-1) },
            { new Point(-1,0), new Point(0,0), new Point(0,-1), new Point(0,1) }
        },
        // J (type 3)
        {
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(1,1) },
            { new Point(0,-1), new Point(0,0), new Point(0,1), new Point(1,-1) },
            { new Point(-1,-1), new Point(-1,0), new Point(0,0), new Point(1,0) },
            { new Point(0,-1), new Point(-1,1), new Point(0,0), new Point(0,1) }
        },
        // L (type 4)
        {
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(-1,1) },
            { new Point(0,-1), new Point(0,0), new Point(0,1), new Point(1,1) },
            { new Point(-1,0), new Point(0,0), new Point(1,0), new Point(1,-1) },
            { new Point(0,-1), new Point(-1,-1), new Point(0,0), new Point(0,1) }
        },
        // S (type 5)
        {
            { new Point(0,0), new Point(1,0), new Point(-1,1), new Point(0,1) },
            { new Point(0,-1), new Point(0,0), new Point(1,0), new Point(1,1) },
            { new Point(0,0), new Point(1,0), new Point(-1,1), new Point(0,1) },
            { new Point(0,-1), new Point(0,0), new Point(1,0), new Point(1,1) }
        },
        // Z (type 6)
        {
            { new Point(-1,0), new Point(0,0), new Point(0,1), new Point(1,1) },
            { new Point(1,-1), new Point(0,0), new Point(1,0), new Point(0,1) },
            { new Point(-1,0), new Point(0,0), new Point(0,-1), new Point(1,-1) },
            { new Point(0,-1), new Point(0,0), new Point(1,0), new Point(1,1) }
        }
    };

    public static final Color[] COLORS = {
        Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.BLUE,
        Color.ORANGE, Color.GREEN, Color.RED
    };
}
