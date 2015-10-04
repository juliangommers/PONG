
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

	// Window settings
	private static int contHeight = 600;
	private static int contWidth = 800;

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
	private float[] positionScoreP1 = {contWidth/2-50, 50};
	private float[] positionScoreP2 = {contWidth/2+50, 50};
	private Font font;
	private TrueTypeFont ttf;
	private TrueTypeFont ttf_big;

	// Maximum bounce off angle (now 75º)
	private static double MAXBOUNCEANGLE = 5 * Math.PI/12;

	// Starting direction of the ball (starts with random side)
	private double ballDx = (Math.random() <= 0.5) ? 5 : -5;
	private double ballDy = 0;
	private double ballSpeed = 5;
	private double playerSpeed = 5;
	private double increasePerBounce = 0.25;

	/**
	 * Constructor of StartGame
	 * @param gamename The name of the game.
	 */
	public StartGame(String gamename) {
		super(gamename);
	}

	/**
	 * Calculate the angle the ball will bounce off from.<br />
	 * This is calculated in the following steps:
	 * <ol>
	 * <li>Calculate the position of the paddles' middle. </li>
	 * <li>Subtract the balls' position from the paddles' middle position. </li>
	 * <li>Normalise the intersection point (creates a percentage). </li>
	 * <li>Calculate the angle based on the maximum bounce off angle. </li>
	 * </ol>
	 * @param player The player from which the ball should bounce away.
	 * @return The directions' angle (in radiants).
	 */
	private double getBounceAngle(Shape player){
		double paddleMiddle = (player.getY() + (player.getHeight()/2));
		double relativeIntersection = paddleMiddle - ball.getY();
		double normalisedrelativeIntersection = (relativeIntersection/(player1.getHeight()/2));
		double bounceAngle = normalisedrelativeIntersection * MAXBOUNCEANGLE;
		return bounceAngle;
	}

	/**
	 * Resets the original settings of the ball.
	 * @param container The GameContainer of the game.
	 * @param direction The direction the ball should take.
	 */
	private void resetBall(GameContainer container, double direction){
		positionBall[0] = (float) (container.getWidth()/2.0);
		positionBall[1] = (float) (container.getHeight()/2.0);
		ballSpeed = 5;
		ballDx = direction*ballSpeed;
		ballDy = 0;

	}

	private void dashedLine(Graphics g){
		for (int i = 0; i < contHeight; i+=25) {
			g.drawLine(contWidth / 2, i, contWidth / 2, (float)(i+12.5));
		}
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
		font = new Font("Verdana", Font.BOLD, 30);
		ttf = new TrueTypeFont(font, true);
		ttf_big = new TrueTypeFont(new Font("Verdana", Font.BOLD, 60), true);
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
		if (input.isKeyDown(Input.KEY_S) && player1.getMaxY() <= container.getHeight()) {
			positionP1[1] = positionP1[1] + (float)(playerSpeed);
			player1.setLocation(positionP1[0], positionP1[1]);
		}
		if (input.isKeyDown(Input.KEY_W) && player1.getMinY() >= 0) {
			positionP1[1] = positionP1[1] - (float)(playerSpeed);
			player1.setLocation(positionP1[0], positionP1[1]);
		}

		// Player interaction of Player 2
		if (input.isKeyDown(Input.KEY_DOWN) && player2.getMaxY() <= container.getHeight()) {
			positionP2[1] = positionP2[1] + (float)(playerSpeed);
			player2.setLocation(positionP2[0], positionP2[1]);
		}
		if (input.isKeyDown(Input.KEY_UP) && player2.getMinY() >= 0) {
			positionP2[1] = positionP2[1] - (float)(playerSpeed);
			player2.setLocation(positionP2[0], positionP2[1]);
		}
		if(input.isKeyDown(Input.KEY_ESCAPE)){
			container.exit();
		}
		if(input.isKeyPressed(Input.KEY_P)){
//			if(!container.isPaused())
//				container.pause();
//			else
//				container.resume();
			container.setPaused(!container.isPaused());
		}

		/********************
		 * BOUNCE MECHANISM *
		 ********************/
		// Bounce back from the paddle.
		if (ball.intersects(player1)) {
			ballDx = ballSpeed * Math.cos( getBounceAngle(player1) );
			ballDy = ballSpeed * -Math.sin( getBounceAngle(player1) );
			ballSpeed += increasePerBounce;
			//			System.out.println("Bounce Player 1");
		} else if (ball.intersects(player2)) {
			ballDx = ballSpeed * -Math.cos( getBounceAngle(player2) );
			ballDy = ballSpeed * -Math.sin( getBounceAngle(player2) );
			ballSpeed += increasePerBounce;
			//			System.out.println("Bounce Player 2");
		}

		// Bounce off the edges
		if(ball.getMinY() <= 0.0 || ball.getMaxY() >= (float)container.getHeight()){
			ballDy = -ballDy;
		}

		/*********************
		 * SCORES INDICATION *
		 *********************/
		// Keep the scores up to date
		if (ball.getMinX() <= (float) 0) {
			scores[1]++;
			resetBall(container, -1);
		}else if (ball.getMaxX() >= (float)container.getWidth()) {
			scores[0]++;
			resetBall(container, 1);
		}

		/************
		 * MOVEMENT *
		 ************/
		// Add the Dx or Dy to the coordinate
		if (ball.getMinX() >= 0 && ball.getMaxX() <= container.getWidth() && !container.isPaused()) {
			positionBall[0] += ballDx;
			positionBall[1] += ballDy;
		}
		ball.setCenterX(positionBall[0]);
		ball.setCenterY(positionBall[1]);
		//		ball.setLocation(positionBall[0], positionBall[1]);
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

		dashedLine(g);

		ttf.drawString(positionScoreP1[0]-10, positionScoreP1[1], Integer.toString(scores[0]));
		ttf.drawString(positionScoreP2[0]-10, positionScoreP2[1], Integer.toString(scores[1]));
		
		String pauseString = "PAUSE";
		
		if(container.isPaused())
			ttf_big.drawString((container.getWidth()/2f)-(ttf_big.getWidth(pauseString)/2f), (container.getHeight()/2f)-(ttf_big.getHeight()/2f), pauseString);

	}

	public static void main(String[] args) {
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new StartGame("PONG"));
			appgc.setTargetFrameRate(60);
			appgc.setDisplayMode(contWidth, contHeight, false);
			appgc.setAlwaysRender(true);
			appgc.setVSync(true);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}