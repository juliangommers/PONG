
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
	Ball ball;

	// Load the scores
	Scores scores;

	// Load the players
	private Player player1;
	private Player player2;

	// Load the info screens
	private InfoText info;

	// Load sounds
	private Sound plop;
	private Sound beep;

	// Speed settings
	private double increasePerBounce;

	private boolean gameStarted;
	private int gameType;
	private int level;

	// Keep track of the current frame
	int currentFrame;

	/**
	 * Constructor of StartGame
	 * @param gamename The name of the game.
	 */
	public StartGame(String gamename) {
		super(gamename);
		increasePerBounce = 0.25;
		gameStarted = false;
		gameType = 0;
		level = 0;
		currentFrame = 0;
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
		double paddleMiddle = player.getCenterY();
		double relativeIntersection = paddleMiddle - ball.getCenterY();
		double normalisedrelativeIntersection = (relativeIntersection/(player1.getHeight()/2));
		double bounceAngle = normalisedrelativeIntersection * Ball.maxBounceAngle;
		return bounceAngle;
	}

	/**
	 * Initial settings
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		// don't show the fps by default
		container.setShowFPS(false);

		// initialise the ball
		ball = new Ball();

		// initialise the texts
		info = new InfoText();

		// initialise the scores
		scores = new Scores();

		// initialise the players
		player1 = new Player(contWidth/10f, contHeight/3f);
		player2 = new Player(((contWidth/10f)*9)-(contWidth/40f), contHeight/3f);

		// load the fonts in different sizes
		Font font20 = info.loadFont("src/media/Arial_Black.ttf", Font.BOLD, 20);
		Font font30 = info.loadFont("src/media/Arial_Black.ttf", Font.BOLD, 30);
		Font font50 = info.loadFont("src/media/Arial_Black.ttf", Font.BOLD, 50);

		// initialize the used fonts
		info.playerFont			= new TrueTypeFont( font20 , true);
		info.scoreFont 			= new TrueTypeFont( font30 , true);
		info.pauseFont 			= new TrueTypeFont( font50 , true);
		info.pongFont 			= new TrueTypeFont( font50 , true);
		info.bounceFont			= new TrueTypeFont( font20 , true);
		info.predictionFont		= new TrueTypeFont( font30 , true);
		info.levelFont			= new TrueTypeFont( font30 , true);

		// initialize the sounds
		plop = new Sound("media/8bit_plop.wav");
		beep = new Sound("media/8bit_beep.wav");

		// screen is paused by default to create a menu situation
		container.pause();
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
		if (input.isKeyDown(Input.KEY_W) && player1.getMinY() > 0 && !container.isPaused() && gameStarted && gameType != 1 && (gameType == 2 || gameType == 3)) {
			player1.up();
		}
		if (input.isKeyDown(Input.KEY_S) && player1.getMaxY() < contHeight && !container.isPaused() && gameStarted && gameType != 1 && (gameType == 2 || gameType == 3)) {
			player1.down();
		}

		// Player interaction of Player 2
		if (input.isKeyDown(Input.KEY_UP) && player2.getMinY() > 0 && !container.isPaused() && gameStarted) {
			player2.up();
			if(gameType == 3){
				level = 2;
			}
		}
		if (input.isKeyDown(Input.KEY_DOWN) && player2.getMaxY() < contHeight && !container.isPaused() && gameStarted) {
			player2.down();
			if(gameType == 3){
				level = 2;
			}
		}

		// User can exit the game
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
                    if (container.isPaused()){
                    container.exit();
                    }else{
                        container.setPaused(!container.isPaused());
                    }
		}

		// User can pause the game
		if(input.isKeyPressed(Input.KEY_P)){
			if(gameStarted)
				container.setPaused(!container.isPaused());
		}

		// User can reset the game
		if(input.isKeyPressed(Input.KEY_R)){
			if(gameStarted){
				container.setPaused(true);
				gameType = 0;
				level = 0;
				gameStarted = false;
				player1.setX(contWidth/10f);
				player1.setY(contHeight/3f);
				player2.setX(((contWidth/10f)*9)-(contWidth/40f));
				player2.setY(contHeight/3f);
				ball.setDx(0);
				ball.setDy(0);
				ball.resetBall((Math.random() <= 0.5) ? 1 : -1);
				info.prediction = false;
				info.predictionTraces = false;
				container.setVSync(true);
				container.setTargetFrameRate(60);
				scores.setScores( new int[]{0,0} );
			}
		}

		// User can reset the game
		if(input.isKeyPressed(Input.KEY_F)){
			container.setFullscreen( !container.isFullscreen() );
			// set the height and width of the container as the height and width of the container.. duh
		}

		// Display FPS by pressing 1
		if(input.isKeyPressed(Input.KEY_0)){
			container.setShowFPS(!container.isShowingFPS());
		}

		// Single game
		if(input.isKeyPressed(Input.KEY_1)){
			if(!gameStarted && gameType == 0){
				gameType = 1;
			}
			else if(gameType == 1){

				level = 1;
				gameStarted = true;
				container.resume();
			}
		}

		// Multi player game
		if(input.isKeyPressed(Input.KEY_2)){
			if(!gameStarted && gameType == 0){
				gameType = 2;
				level = 1;
				gameStarted = true;
				container.resume();
			}
			else if(gameType == 1){

				level = 2;
				gameStarted = true;
				container.resume();
			}
		}

		// Insane mode
		if(input.isKeyPressed(Input.KEY_3)){
			if(!gameStarted && gameType == 0){
				gameType = 3;
				gameStarted = true;
				container.setVSync(false);
				container.setTargetFrameRate(Integer.MAX_VALUE);
				container.resume();
			}
			else if(gameType == 1){

				level = 3;
				gameStarted = true;
				container.resume();
			}
		}

		// Show prediction
		if(input.isKeyPressed(Input.KEY_8)){
                    if (info.prediction && info.predictionTraces){
			info.predictionTraces = !info.predictionTraces;
                    }else{
                        info.prediction = !info.prediction;
                    }
		}

		// Show prediction traces
		if(input.isKeyPressed(Input.KEY_9)){
                    if(info.prediction && !info.predictionTraces){
                        info.predictionTraces = !info.predictionTraces;
                    }else{
                        info.prediction = !info.prediction;
			info.predictionTraces = !info.predictionTraces;
                    }
                }


		/********************
		 * BOUNCE MECHANISM *
		 ********************/
		// Bounce back from the paddle.
		if (ball.ball.intersects(player1.getPlayer())) {
			plop.play();
			ball.setDx( ball.getSpeed() * Math.cos( getBounceAngle(player1.getPlayer()) ) );
			ball.setDy( ball.getSpeed() * -Math.sin( getBounceAngle(player1.getPlayer()) ) );
			ball.setBallSpeed(ball.getSpeed() + increasePerBounce);
		} else if (ball.ball.intersects(player2.getPlayer())) {
			plop.play();
			ball.setDx( ball.getSpeed() * -Math.cos( getBounceAngle(player2.getPlayer()) ) );
			ball.setDy( ball.getSpeed() * -Math.sin( getBounceAngle(player2.getPlayer()) ) );
			ball.setBallSpeed(ball.getSpeed() + increasePerBounce);
		}

		// Bounce off the edges
		if(ball.getMinY() <= 0.0 || ball.getMaxY() > (double) contHeight){
			ball.setDy(-ball.getDy());
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

		/**************************
		 * ARTIFICIAL INTELIGENCE *
		 **************************/
		// LEVEL BEGINNER
		if(gameStarted && gameType == 1 && level == 1 && !container.isPaused() && ball.getDx() < 0.0){
			if(ball.getCenterY() < player1.getCenterY()){
				player1.up();
			}
			if(ball.getCenterY() > player1.getCenterY()){
				player1.down();
			}
		}

		// LEVEL INTERMEDIATE
		if(gameStarted && gameType == 1 && level == 2 && !container.isPaused()){
			if(ball.getCenterX() < contWidth/2f){
				if(ball.getCenterY() < player1.getCenterY()){
					player1.up();
				}
				if(ball.getCenterY() > player1.getCenterY()){
					player1.down();
				}
			}
			if(ball.getCenterX() > contWidth/2f){
				if(player1.getCenterY() > contHeight/2f){
					player1.up();
				}
				if(player1.getCenterY() < contHeight/2f){
					player1.down();
				}
			}
		}

		// LEVEL EXPERT
		if(gameStarted && gameType == 1 && level == 3 && !container.isPaused()){
			if(ball.getDx() <= 0){
				int off = 0;
				if( player2.getCenterY() > contHeight/2f)
					off = -20;
				else if( player2.getCenterY() < contHeight/2f)
					off = 20;

				if( player1.getCenterY()+off > ball.predictY(player1) ){
					player1.up();
				}
				if( player1.getCenterY()+off < ball.predictY(player1) ){
					player1.down();
				}
			}
			if(ball.getDx() > 0){
				if(player1.getCenterY() > contHeight/2f){
					player1.up();
				}
				if(player1.getCenterY() < contHeight/2f){
					player1.down();
				}
			}

		}

		// keep track of the frames
		if(currentFrame < 60)
			currentFrame++;
		else
			currentFrame = 0;

		// in case of insanemode
		if(gameStarted && gameType == 3 && !container.isPaused()){
			// player 1
			if(ball.getDx() <= 0){
				int off = 0;
				if( player2.getCenterY() > contHeight/2f)
					off = -20;
				else if( player2.getCenterY() < contHeight/2f)
					off = 20;

				if( player1.getCenterY()+off > ball.predictY(player1) ){
					player1.up();
				}
				if( player1.getCenterY()+off < ball.predictY(player1) ){
					player1.down();
				}
			}
			if(ball.getDx() > 0){
				if(player1.getCenterY() > contHeight/2f){
					player1.up();
				}
				if(player1.getCenterY() < contHeight/2f){
					player1.down();
				}
			}

			// player 2
			if(ball.getDx() >= 0 && level != 2){
				int off = 0;
				if( player1.getCenterY() >= contHeight/2f)
					off = -20;
				else if( player1.getCenterY() < contHeight/2f)
					off = 20;

				if( player2.getCenterY()+off > ball.predictY(player2) ){
					player2.up();
				}
				if( player2.getCenterY()+off < ball.predictY(player2) ){
					player2.down();
				}
			}
			if(ball.getDx() < 0 && level != 2){
				if(player2.getCenterY() > contHeight/2f){
					player2.up();
				}
				if(player2.getCenterY() < contHeight/2f){
					player2.down();
				}
			}
		}


		/************
		 * MOVEMENT *
		 ************/
		// Add the Dx or Dy to the coordinate
		if (ball.getMinX() >= 0 && ball.getMaxX() <= contWidth && !container.isPaused() && gameStarted) {
			float x = (float) ( ball.getX() + ball.getDx() );
			float y = (float) ( ball.getY() + ball.getDy() );
			ball.setX( x );
			ball.setY( y );
		}else if(!container.isPaused() && gameStarted){
			ball.setCenterX((float) ball.getX());
			ball.setCenterY((float) ball.getY());
		}

		//		System.out.println(ball.toString(player1));
	}

	/**
	 * Renders the images
	 */
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {

		if(gameStarted){
			g.draw(player1.getPlayer());
			g.setColor(new Color(255, 255, 255));
			g.fill(player1.getPlayer());

			g.draw(player2.getPlayer());
			g.setColor(new Color(255, 255, 255));
			g.fill(player2.getPlayer());

			g.draw(ball.getBall());
			g.setColor(new Color(255, 255, 255));
			g.fill(ball.getBall());

			info.dashedLine(g);


			// Show the scores on the screen
			info.scores(scores);

			// Prediction where the ball will hit
			if((info.prediction || info.predictionTraces) && (ball.getCenterX() > (player1.getCenterX() + ball.getWidth()) && ball.getCenterX() < (player2.getCenterX() - ball.getWidth())) ){
				if(ball.getDx() <= 0)
					ball.predictY(player1);
				if(ball.getDx() >= 0)
					ball.predictY(player2);

				info.predictY(ball, g);
			}

		}

		// show instruction
		if(gameType == 1 && !gameStarted){
			info.levelScreen();
			info.playerInstructions();
		}

		g.setColor(new Color(255, 255, 255));
		// Show the start screen at the beginning of the game
		if(gameType == 0 && !gameStarted){
			info.startScreen();
			info.playerInstructions();
		}

		// Show the pause screen when the game is paused
		if(container.isPaused() && gameStarted)
			info.pauseScreen();

	}

	public static void main(String[] args) {
		NativeLoader.load();
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
