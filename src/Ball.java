import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;


public class Ball {

	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;
	
	Shape ball = null;
	private float[] positionBall = {contWidth/2f, contHeight/2f};

	// Maximum bounce off angle (now 75º)
	static double MAXBOUNCEANGLE = 5 * Math.PI/12;
	
	// Starting direction of the ball (starts with random side)
	private double ballDx = (Math.random() <= 0.5) ? 5 : -5;
	private double ballDy = 0;
	private double ballSpeed = 5;
	
	
	/**
	 * Ball Constructor
	 */
	public Ball(){
		ball = new Circle(positionBall[0], positionBall[1], 10);
	}
	
	/**
	 * Resets the original settings of the ball.
	 * @param container The GameContainer of the game.
	 * @param direction The direction the ball should take.
	 */
	public void resetBall(double direction){
		this.setX(contWidth/2f);
		this.setY(contHeight/2f);
		ballSpeed = 5;
		ballDx = direction * ballSpeed;
		ballDy = 0;

	}
	
	
	/*********************
	 * GETTERS & SETTERS *
	 *********************/
	
	/**
	 * @return the ball
	 */
	public Shape getBall() {
		return ball;
	}

	/**
	 * @return
	 */
	public double getX() {
//		return this.positionBall[0];
		return ball.getX();
	}
	
	/**
	 * @return
	 */
	public void setX(float x) {
//		this.positionBall[0] = x;
		ball.setX(x);
	}
	
	/**
	 * @return
	 */
	public double getY() {
		return ball.getY();
	}
	
	/**
	 * @return
	 */
	public void setY(float y) {
//		this.positionBall[1] = y;
		ball.setY(y);
	}

	/**
	 * @return the ballDx
	 */
	public double getBallDx() {
		return ballDx;
	}
	
	/**
	 * @param ballDx the ballDx to set
	 */
	public void setBallDx(double ballDx) {
		this.ballDx = ballDx;
	}

	/**
	 * @return the ballDy
	 */
	public double getBallDy() {
		return ballDy;
	}

	/**
	 * @param ballDy the ballDy to set
	 */
	public void setBallDy(double ballDy) {
		this.ballDy = ballDy;
	}

	/**
	 * @return the ballSpeed
	 */
	public double getBallSpeed() {
		return ballSpeed;
	}

	/**
	 * @param ballSpeed the ballSpeed to set
	 */
	public void setBallSpeed(double ballSpeed) {
		this.ballSpeed = ballSpeed;
	}

	/**
	 * @return Get the minimum X value
	 */
	public double getMinX() {
		return ball.getMinX();
	}
	
	/**
	 * @return Get the minimum Y value
	 */
	public double getMinY() {
		return ball.getMinY();
	}
	
	/**
	 * @return Get the maximum X value
	 */
	public double getMaxX() {
		return ball.getMaxX();
	}
	
	/**
	 * @return Get the maximum Y value
	 */
	public double getMaxY() {
		return ball.getMaxY();
	}

	/**
	 * @param x
	 */
	public void setCenterX(float x) {
		ball.setCenterX(x);
	}

	/**
	 * @param y
	 */
	public void setCenterY(float y) {
		ball.setCenterY(y);
	}
}