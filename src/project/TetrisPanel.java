package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TetrisPanel extends JPanel
        implements ActionListener, KeyListener, MouseListener {

    private final int ROWS = 20, COLS = 10, BLOCK = 30, SIDE = 180;

    private Color[][] board;
    private javax.swing.Timer timer;
    private Random random = new Random();

    private Tetromino current;
    private Queue<Tetromino> nextQueue = new LinkedList<>();

    private Stack<Color[][]> undoStack = new Stack<>();
    private boolean paused = false, gameOver = false;
    private int score = 0;

    private Rectangle btnResume = new Rectangle(20, 150, 130, 35);
    private Rectangle btnRestart = new Rectangle(20, 200, 130, 35);
    private Rectangle btnUndo = new Rectangle(20, 250, 130, 35);
    private Rectangle btnQuit = new Rectangle(20, 300, 130, 35);

    public TetrisPanel(Color[][] board) {
        this.board = board;
        setPreferredSize(new Dimension(SIDE + COLS * BLOCK, ROWS * BLOCK));
        setBackground(Color.BLACK);

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);

        nextQueue.add(generatePiece());
        spawnPiece();

        timer = new javax.swing.Timer(500, this);
        timer.start();
    }

    private Tetromino generatePiece() {
        return new Tetromino(random.nextInt(7));
    }

    private void spawnPiece() {
        current = nextQueue.poll();
        nextQueue.add(generatePiece());

        current.x = COLS / 2 - 2;
        current.y = 0;

        if (!isValidMove(current.x, current.y, current.rotation)) {
            gameOver = true;
            timer.stop();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLeftPanel(g);
        drawBoard(g);
        drawCurrentPiece(g);
        drawOverlays(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(SIDE, 0, COLS * BLOCK, ROWS * BLOCK);

        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != null) {
                    g.setColor(board[r][c]);
                    g.fillRect(SIDE + c * BLOCK, r * BLOCK, BLOCK, BLOCK);
                }
                g.setColor(Color.DARK_GRAY);
                g.drawRect(SIDE + c * BLOCK, r * BLOCK, BLOCK, BLOCK);
            }
    }

    private void drawCurrentPiece(Graphics g) {
        if (gameOver) return;

        g.setColor(current.color);
        for (Point p : current.shape[current.rotation]) {
            int x = current.x + p.x, y = current.y + p.y;
            g.fillRect(SIDE + x * BLOCK, y * BLOCK, BLOCK, BLOCK);
        }
    }

    private void drawLeftPanel(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, SIDE, ROWS * BLOCK);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("SCORE:", 20, 40);
        g.drawString("" + score, 20, 70);

        g.drawString("STATUS:", 20, 110);
        g.drawString(gameOver ? "GAME OVER" :
                     paused ? "PAUSED" : "PLAYING", 20, 140);

        drawButton(g, btnResume, "RESUME");
        drawButton(g, btnRestart, "RESTART");
        drawButton(g, btnUndo, "UNDO (U)");
        drawButton(g, btnQuit, "QUIT");

        drawNextPreview(g);
    }

    private void drawButton(Graphics g, Rectangle r, String text) {
        g.setColor(Color.WHITE);
        g.fillRect(r.x, r.y, r.width, r.height);

        g.setColor(Color.BLACK);
        g.drawRect(r.x, r.y, r.width, r.height);
        g.drawString(text, r.x + 20, r.y + 22);
    }

    private void drawNextPreview(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("NEXT:", 20, 360);

        Tetromino next = nextQueue.peek();
        if (next == null) return;

        for (Point p : next.shape[0]) {
            int px = 35 + (p.x + 2) * 20;
            int py = 390 + (p.y + 2) * 20;

            g.setColor(next.color);
            g.fillRect(px, py, 20, 20);

            g.setColor(Color.GRAY);
            g.drawRect(px, py, 20, 20);
        }
    }

    private void drawOverlays(Graphics g) {
        if (paused && !gameOver) drawOverlay(g, "PAUSED", Color.WHITE);
        if (gameOver) drawOverlay(g, "GAME OVER", Color.RED);
    }

    private void drawOverlay(Graphics g, String text, Color color) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(SIDE, 0, COLS * BLOCK, ROWS * BLOCK);

        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(text, SIDE + 20, 200);
    }

    private boolean isValidMove(int newX, int newY, int rot) {
        for (Point p : current.shape[rot]) {
            int x = newX + p.x, y = newY + p.y;

            if (x < 0 || x >= COLS || y >= ROWS) return false;
            if (y >= 0 && board[y][x] != null) return false;
        }
        return true;
    }

    private void pushUndoState() {
        Color[][] copy = new Color[ROWS][COLS];
        for (int r = 0; r < ROWS; r++)
            System.arraycopy(board[r], 0, copy[r], 0, COLS);
        undoStack.push(copy);
    }

    private void lockPiece() {
        pushUndoState();
        score += 10;

        for (Point p : current.shape[current.rotation]) {
            int x = current.x + p.x, y = current.y + p.y;

            if (y < 0) {
                gameOver = true;
                timer.stop();
                repaint();
                return;
            }
            board[y][x] = current.color;
        }

        clearLines();
        spawnPiece();
    }

    private void clearLines() {
        int cleared = 0;

        for (int r = ROWS - 1; r >= 0; r--) {

            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == null) { full = false; break; }

            if (full) {
                cleared++;
                for (int row = r; row > 0; row--)
                    System.arraycopy(board[row - 1], 0, board[row], 0, COLS);
                Arrays.fill(board[0], null);
                r++;
            }
        }

        int[] scores = {0, 100, 300, 500, 800};
        if (cleared > 0) score += scores[Math.min(cleared, 4)];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver)
            if (isValidMove(current.x, current.y + 1, current.rotation))
                current.y++;
            else
                lockPiece();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver || paused) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                if (isValidMove(current.x - 1, current.y, current.rotation))
                    current.x--;
            }
            case KeyEvent.VK_RIGHT -> {
                if (isValidMove(current.x + 1, current.y, current.rotation))
                    current.x++;
            }
            case KeyEvent.VK_UP -> {
                int newRot = (current.rotation + 1) % 4;
                if (isValidMove(current.x, current.y, newRot))
                    current.rotation = newRot;
            }
            case KeyEvent.VK_DOWN -> {
                if (isValidMove(current.x, current.y + 1, current.rotation))
                    current.y++;
            }
            case KeyEvent.VK_U -> undoLast();
        }

        repaint();
    }

    private void undoLast() {
        if (undoStack.isEmpty()) return;
        Color[][] prev = undoStack.pop();
        for (int r = 0; r < ROWS; r++)
            System.arraycopy(prev[r], 0, board[r], 0, COLS);
        repaint();
    }

    private void restartGame() {
        for (int r = 0; r < ROWS; r++)
            Arrays.fill(board[r], null);

        undoStack.clear();
        score = 0;
        paused = false;
        gameOver = false;

        nextQueue.clear();
        nextQueue.add(generatePiece());
        spawnPiece();
        timer.start();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX(), y = e.getY();

        if (btnResume.contains(x, y)) paused = false;
        else if (btnRestart.contains(x, y)) restartGame();
        else if (btnUndo.contains(x, y)) undoLast();
        else if (btnQuit.contains(x, y)) System.exit(0);

        repaint();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
