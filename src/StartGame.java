
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
    private int xBall = 400;
    private int yBall = 300;
    private int xP1 = 100;
    private int yP1 = 200;
    private int xP2 = 675;
    private int yP2 = 200;
    private String ballDirection = "l";

    public StartGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setShowFPS(true);
        player1 = new Rectangle(xP1, yP1, 25, 200);
        player2 = new Rectangle(xP2, yP2, 25, 200);
        ball = new Circle(xBall, yBall, 10);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_DOWN) && player2.getY() <= (container.getHeight() - player2.getHeight())) {
            yP2++;
            player2.setLocation(xP2, yP2);
        }
        if (input.isKeyDown(Input.KEY_UP) && player2.getY() >= 0) {
            yP2--;
            player2.setLocation(xP2, yP2);
        }
        if (input.isKeyDown(Input.KEY_S) && player1.getY() <= (container.getHeight() - player1.getHeight())) {
            yP1++;
            player1.setLocation(xP1, yP1);
        }
        if (input.isKeyDown(Input.KEY_W) && player1.getY() >= 0) {
            yP1--;
            player1.setLocation(xP1, yP1);
        }

        if (ball.intersects(player1)){
            ballDirection="r";
        }else if(ball.intersects(player2)){
            ballDirection="l";
        }
        if (ballDirection.equals("r") && ball.getX() <= (container.getWidth())-ball.getWidth()){
            xBall++;
        }else if (ballDirection.equals("l") &&  ball.getX() >= 0){
            xBall--;
        }
        ball.setLocation(xBall, yBall);
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
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new StartGame("PONG"));
            appgc.setDisplayMode(800, 600, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
