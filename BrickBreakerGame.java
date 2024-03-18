package brick;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGame extends JFrame implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 20;
    private final int BALL_DIAMETER = 20;
    private final int BRICK_WIDTH = 60;
    private final int BRICK_HEIGHT = 30;
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private final int DELAY = 10;

    private Timer timer;
    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX = WIDTH / 2 - BALL_DIAMETER / 2;
    private int ballY = HEIGHT - 100;
    private int ballXSpeed = 1;
    private int ballYSpeed = -1;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int score = 0;
    private boolean[][] bricks;

    public BrickBreakerGame() {
        setTitle("Brick Breaker");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bricks = new boolean[BRICK_ROWS][BRICK_COLS];
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j] = true;
            }
        }

        timer = new Timer(DELAY, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = false;
                }
            }
        });

        setFocusable(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw paddle
        g2d.setColor(Color.BLUE);
        g2d.fillRect(paddleX, HEIGHT - 50, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g2d.setColor(Color.RED);
        g2d.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);

        // Draw bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j]) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(j * BRICK_WIDTH + 20, i * BRICK_HEIGHT + 50, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }

        // Draw score
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 10, 20);
    }

    public void actionPerformed(ActionEvent e) {
        if (leftPressed && paddleX > 0) {
            paddleX -= 5;
        }
        if (rightPressed && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += 5;
        }

        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Ball collision with walls
        if (ballX <= 0 || ballX >= WIDTH - BALL_DIAMETER) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballY <= 0) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball collision with paddle
        if (ballY + BALL_DIAMETER >= HEIGHT - 50 &&
            ballX + BALL_DIAMETER >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball collision with bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j]) {
                    int brickX = j * BRICK_WIDTH + 20;
                    int brickY = i * BRICK_HEIGHT + 50;
                    Rectangle brickRect = new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                    Rectangle ballRect = new Rectangle(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        ballYSpeed = -ballYSpeed;
                        score += 10;
                    }
                }
            }
        }

        // Game over condition
        if (ballY >= HEIGHT) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!\nYour score: " + score);
            System.exit(0);
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BrickBreakerGame().setVisible(true);
        });
    }
}
