import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;


public class Ball {

	private static final int contHeight = StartGame.contHeight;
	private static final int contWidth = StartGame.contWidth;
	
	Shape ball = null;
	private float[] positionBall = {contWidth/2f, contHeight/2f+5};

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
		this.setCenterX(contWidth/2f);
		this.setCenterY(contHeight/2f);
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
	
	public float getWidth(){
		return ball.getWidth();
	}
	
	public float getHeight(){
		return ball.getHeight();
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
	 *
	 */
	public float getCenterX() {
		return ball.getCenterX();
	}

	/**
	 * @return 
	 *
	 */
	public float getCenterY() {
		return ball.getCenterY();
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
	
	public float getDyDx(){
		return (float)(this.getBallDy()) / (float)(this.getBallDx());
	}
	
	/**
	 * Get the X value from a given y point, following the perpendicular of the direction of the ball.  
	 * @param xPoint
	 * @param yPoint
	 * @param yEnd
	 * @return
	 */
	public float linEqX(float direction, float xPoint, float yPoint, float yEnd) {
		float a = direction;
		float b = ( yPoint - a * xPoint );
		float x = (yEnd - b) / a;
		return x;
	}
	
	/**
	 * Get the Y value from a given x point, following the perpendicular of the direction of the ball.  
	 * @param xPoint
	 * @param yPoint
	 * @param xEnd
	 * @return
	 */
	public float linEqY(float direction, float xPoint, float yPoint, float xEnd) {
		float a = direction;
		float b = ( yPoint - a * xPoint );
		float y = a * xEnd + b;
		return y;
	}
	
	public float predictY(Player player) {
		// the offset next to the paddle
		float offsetX = this.getBallDx() < 0 ? this.getWidth() : -this.getWidth();

		// the offset next to the border
		float offsetY = this.getHeight()/2;

		// position of the ball
		float x1 = this.getCenterX();
		float y1 = this.getCenterY();

		// position where the ball will end up without bouncing
		float x2 = player.getCenterX() + offsetX;
		float y2 = this.linEqY(this.getDyDx(), x1, y1, x2);

		// direction of the ball
		float a = this.getDyDx();
		// ball up or down
		double dy = this.getBallDy();

		// set while counter
		int i = 0;

		// if y2 is out of the frame, it means the ball will bounce
		while((y2 <= 0f || y2 >= contHeight)){
			
			// which border will it bounce off from
			if( (dy > 0 && i % 2 == 0) || (dy < 0 && i % 2 != 0) )
				y1 = contHeight - offsetY;
			if( (dy < 0 && i % 2 == 0) || (dy > 0 && i % 2 != 0) )
				y1 = 0f + offsetY;

			// change direction depending on the bounce round
			int d = (int) Math.pow(-1, i);

			// calculate hitting point on the border +
			x1 = this.linEqX(d*a, x2, y2, y1);

			// recalculate estimated hitting point -
			y2 = this.linEqY(d*-a, x1, y1, x2);

			// increase counter after prediction
			i++;
		}
		
		return y2;
	}
	
	public String toString(float playerX){
		String output = "Ball: \n"
				+ "X=" + this.getCenterX() +"\n"
				+ "Y=" + this.getCenterY() +"\n"
				+ "Dx=" + this.getBallDx() +"\n"
				+ "Dy=" + this.getBallDy() +"\n"
				+ "Dx/Dy=" + this.getBallDx()/this.getBallDy() +"\n"
				+ "b=" + ( this.getCenterY() - (float)(this.getBallDx()) / (float)(-this.getBallDy()) * this.getCenterX() ) +"\n"
				;
		return output;
	}
}
