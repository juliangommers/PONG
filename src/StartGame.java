
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class StartGame extends BasicGame {

	// Players and Ball
	private Shape player1 = null;
	private Shape player2 = null;
	private Shape ball = null;

	// Initial location of the players and ball
	private float[] positionBall = {400,300};
	private float[] positionP1 = {100,200};
	private float[] positionP2 = {675,200};

	// Let's start the game at 0 - 0
	private int[] scores = {0,0};

	// Initial settings of the textbox
	private float[] positionTextbox = {362, 50};
	private Font font;
	private TrueTypeFont ttf;

	// Maximum bounce off angle (now 75º)
	private static double MAXBOUNCEANGLE = 5 * Math.PI/12;

	// Starting direction of the ball
	private double ballDx = 1;
	private double ballDy = 0;

	/**
	 * Constructor of StartGame
	 * @param gamename The name of the game.
	 */
	public StartGame(String gamename) {
		super(gamename);
	}

	public double getBounceAngle(Shape player){
		double paddleMiddle = (player.getY() + (player.getHeight()/2));
		double relativeIntersection = paddleMiddle - ball.getY();
		double normalisedrelativeIntersection = (relativeIntersection/(player1.getHeight()/2));
		double bounceAngle = normalisedrelativeIntersection * MAXBOUNCEANGLE;
		return bounceAngle;
	}

	/**
	 * Initial settings
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		container.setShowFPS(true);
		player1 = new Rectangle(positionP1[0], positionP1[1], 25, 200);
		player2 = new Rectangle(positionP2[0], positionP2[1], 25, 200);
		ball = new Circle(positionBall[0], positionBall[1], 10);
		font = new Font("Verdana", Font.BOLD,30);
		ttf = new TrueTypeFont(font, true);
	}

	/**
	 * Updates all values every cycle
	 */
	@Override
	public void update(GameContainer container, int delta) throws SlickException {

		/**********************
		 * PLAYER INTERACTION *
		 **********************/
		Input input = container.getInput();
		// Player interaction of Player 1
		if (input.isKeyDown(Input.KEY_S) && player1.getY() <= (container.getHeight() - player1.getHeight())) {
			positionP1[1]++;
			player1.setLocation(positionP1[0], positionP1[1]);
		}
		if (input.isKeyDown(Input.KEY_W) && player1.getY() >= 0) {
			positionP1[1]--;
			player1.setLocation(positionP1[0], positionP1[1]);
		}

		// Player interaction of Player 2
		if (input.isKeyDown(Input.KEY_DOWN) && player2.getY() <= (container.getHeight() - player2.getHeight())) {
			positionP2[1]++;
			player2.setLocation(positionP2[0], positionP2[1]);
		}
		if (input.isKeyDown(Input.KEY_UP) && player2.getY() >= 0) {
			positionP2[1]--;
			player2.setLocation(positionP2[0], positionP2[1]);
		}

		/********************
		 * BOUNCE MECHANISM *
		 ********************/
		// Bounce back from the paddle.
		if (ball.intersects(player1)) {
			ballDx = Math.cos( getBounceAngle(player1) );
			ballDy = -Math.sin( getBounceAngle(player1) );
			System.out.println("Bounce Player 1");
		} else if (ball.intersects(player2)) {
			ballDx = -Math.cos( getBounceAngle(player2) );
			ballDy = -Math.sin( getBounceAngle(player2) );
			System.out.println("Bounce Player 2");
		}

		/********************
		 * SCORES INDICATION *
		 *********************/
		// Keep the scores up to date
		if (ball.getMaxX() >= container.getWidth()) {
			scores[0]++;
			System.out.println("P1: " + scores[0] + " score P2: " + scores[1]);
			positionBall[0] = 400;
			positionBall[1] = 300;
		} else if (ball.getX() == 0) {
			scores[1]++;
			System.out.println("P1: " + scores[0] + " score P2: " + scores[1]);
			positionBall[0] = 400;
			positionBall[1] = 300;
		}

		/***********
		 * MOVEMENT *
		 ************/
		// Add the Dx or Dy to the coordinate
		if (ball.getX() >= 0 && ball.getX() <= (container.getWidth()) - ball.getWidth()) {
			positionBall[0] += ballDx;
			positionBall[1] += ballDy;
		}
		ball.setLocation(positionBall[0], positionBall[1]);
	}

	/**
	 * Renders the images
	 */
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
		ttf.drawString(positionTextbox[0], positionTextbox[1], scores[0] + " - " + scores[1]);
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