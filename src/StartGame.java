
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class StartGame extends BasicGame {

    private Shape player1 = null;
    private Shape player2 = null;
    private Shape ball = null;
    private float[] positionBall = {400,300};
    private float[] positionP1 = {100,200};
    private float[] positionP2 = {675,200};
    private String ballDirection = "l";
    private int[] scores = {0,0};
    private float[] positionTextbox = {300,50};

    public StartGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setShowFPS(true);
        player1 = new Rectangle(positionP1[0], positionP1[1], 25, 200);
        player2 = new Rectangle(positionP2[0], positionP2[1], 25, 200);
        ball = new Circle(positionBall[0], positionBall[1], 10);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_DOWN) && player2.getY() <= (container.getHeight() - player2.getHeight())) {
            positionP2[1]++;
            player2.setLocation(positionP2[0], positionP2[1]);
        }
        if (input.isKeyDown(Input.KEY_UP) && player2.getY() >= 0) {
            positionP2[1]--;
            player2.setLocation(positionP2[0], positionP2[1]);
        }
        if (input.isKeyDown(Input.KEY_S) && player1.getY() <= (container.getHeight() - player1.getHeight())) {
            positionP1[1]++;
            player1.setLocation(positionP1[0], positionP1[1]);
        }
        if (input.isKeyDown(Input.KEY_W) && player1.getY() >= 0) {
            positionP1[1]--;
            player1.setLocation(positionP1[0], positionP1[1]);
        }

        if (ball.intersects(player1)) {
            ballDirection = "r";
        } else if (ball.intersects(player2)) {
            ballDirection = "l";
        }
        if (ballDirection.equals("r") && ball.getX() <= (container.getWidth()) - ball.getWidth()) {
            positionBall[0]++;
        } else if (ballDirection.equals("l") && ball.getX() >= 0) {
            positionBall[0]--;
        }

        if (ball.getMaxX() >= container.getWidth()) {
            scores[0]++;
            System.out.println("P1: " + scores[0] + " score P2: " + scores[1]);
            positionBall[0] = 400;
            positionBall[1] = 300;
            ballDirection = "l";
        } else if (ball.getX() == 0) {
            scores[1]++;
            System.out.println("P1: " + scores[0] + " score P2: " + scores[1]);
            positionBall[0] = 400;
            positionBall[1] = 300;
            ballDirection = "r";
        }

        ball.setLocation(positionBall[0], positionBall[1]);
    }

    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {

        g.draw(player1);
        g.setColor(new Color(255, 255, 255));
        g.fill(player1);
        g.draw(player2);
        g.setColor(new Color(255, 255, 255));
        g.fill(player2);
        g.draw(ball);
        g.setColor(new Color(255, 255, 255));
        g.fill(ball);
        g.drawLine(400, 0, 400, 600);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new StartGame("PONG"));
            appgc.setTargetFrameRate(200);
            appgc.setDisplayMode(800, 600, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
