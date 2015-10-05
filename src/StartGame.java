
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
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;

public class StartGame extends BasicGame {

	// Window settings
	static int contHeight = 600;
	static int contWidth = 800;

	// Load the ball
	Ball ball = new Ball();
	
	// Load the scores
	Scores scores = new Scores();
	
	// Load the players
	private Player player1;
	private Player player2;
	
	// Load the info screens
	private InfoText info = new InfoText();
	
	// Load sounds
	private Sound plop;
	private Sound beep;

	// Speed settings
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
		double bounceAngle = normalisedrelativeIntersection * Ball.MAXBOUNCEANGLE;
		return bounceAngle;
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
		container.setShowFPS(false);
		player1 = new Player(contWidth/10f, contHeight/3f);
		player2 = new Player(((contWidth/10f)*9)-(contWidth/40f), contHeight/3f);
		info.scoreFont = new TrueTypeFont( new Font("Verdana", Font.BOLD, 30) , true);
		info.pauseFont = new TrueTypeFont( new Font("Verdana", Font.BOLD, 52) , true);
		container.setSoundVolume(1.0f);
		plop = new Sound("media/8bit_plop.wav");
		beep = new Sound("media/8bit_beep.wav");
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
		if (input.isKeyDown(Input.KEY_W) && player1.getMinY() > 0 && !container.isPaused()) {
			player1.up();
		}
		if (input.isKeyDown(Input.KEY_S) && player1.getMaxY() < contHeight && !container.isPaused()) {
			player1.down();
		}
		
		// Player interaction of Player 2
		if (input.isKeyDown(Input.KEY_UP) && player2.getMinY() > 0 && !container.isPaused()) {
			player2.up();
		}
		if (input.isKeyDown(Input.KEY_DOWN) && player2.getMaxY() < contHeight && !container.isPaused()) {
			player2.down();
		}
		
		// User can exit the game
		if(input.isKeyDown(Input.KEY_ESCAPE)){
			container.exit();
		}

		// User can pause the game
		if(input.isKeyPressed(Input.KEY_P)){
			container.setPaused(!container.isPaused());
		}


		// Display FPS by pressing 1
		if(input.isKeyPressed(Input.KEY_1)){
			container.setShowFPS(!container.isShowingFPS());
		}


		/********************
		 * BOUNCE MECHANISM *
		 ********************/
		// Bounce back from the paddle.
		if (ball.ball.intersects(player1.getPlayer())) {
			plop.play();
			ball.setBallDx( ball.getBallSpeed() * Math.cos( getBounceAngle(player1.getPlayer()) ) );
			ball.setBallDy( ball.getBallSpeed() * -Math.sin( getBounceAngle(player1.getPlayer()) ) );
			ball.setBallSpeed(ball.getBallSpeed() + increasePerBounce);
		} else if (ball.ball.intersects(player2.getPlayer())) {
			plop.play();
			ball.setBallDx( ball.getBallSpeed() * -Math.cos( getBounceAngle(player2.getPlayer()) ) );
			ball.setBallDy( ball.getBallSpeed() * -Math.sin( getBounceAngle(player2.getPlayer()) ) );
			ball.setBallSpeed(ball.getBallSpeed() + increasePerBounce);
		}

		// Bounce off the edges
		if(ball.getMinY() <= 0.0 || ball.getMaxY() > (double)contHeight){
			ball.setBallDy(-ball.getBallDy());
		}


		/*********************
		 * SCORES INDICATION *
		 *********************/
		// Keep the scores up to date
		if (ball.getMinX() <= 0.0) {
			beep.play();
			scores.increaseScoreP2();
			ball.resetBall(-1);
		}else if (ball.getMaxX() >= (float)contWidth) {
			beep.play();
			scores.increaseScoreP1();
			ball.resetBall(1);
		}


		/************
		 * MOVEMENT *
		 ************/
		// Add the Dx or Dy to the coordinate
		if (ball.getMinX() >= 0 && ball.getMaxX() <= contWidth && !container.isPaused()) {
			float x = (float) ( ball.getX() + ball.getBallDx() );
			float y = (float) ( ball.getY() + ball.getBallDy() );
			ball.setX( x );
			ball.setY( y );
		}else if(!container.isPaused()){
			ball.setCenterX((float) ball.getX());
			ball.setCenterY((float) ball.getY());
		}
	}

	/**
	 * Renders the images
	 */
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {

		g.draw(player1.getPlayer());
		g.setColor(new Color(255, 255, 255));
		g.fill(player1.getPlayer());

		g.draw(player2.getPlayer());
		g.setColor(new Color(255, 255, 255));
		g.fill(player2.getPlayer());

		g.draw(ball.getBall());
		g.setColor(new Color(255, 255, 255));
		g.fill(ball.getBall());

		dashedLine(g);

		// Show the scores on the screen
		info.scores(scores);

		// Show the pause screen when the game is paused
		if(container.isPaused())
			info.pauseScreen();

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