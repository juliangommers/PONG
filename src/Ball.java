import java.util.ArrayList;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;


public class Ball {
	// set the sizes
	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	// Ball and location of the Ball
	Shape ball;
	private float[] position;

	// Maximum bounce off angle (now 75º)
	static double maxBounceAngle;

	// Starting direction of the ball (starts with random side)
	private double dx;
	private double dy;
	private double speed;

	// keep track of the bounces going to be made
	ArrayList<float[]> predictionTrace;


	/**
	 * Ball Constructor which initialises the ball
	 */
	public Ball(){
		// set the position
		position = new float[]{contWidth/2f, contHeight/2f+5};

		// determine the movement
		maxBounceAngle = 5 * Math.PI/12;
		dx = (Math.random() <= 0.5) ? 5.0 : -5.0;
		dy = 0.0;
		speed = 5.0;

		// create the ball
		ball = new Circle(position[0], position[1], 10);

		// start a new ArrayList
		predictionTrace = new ArrayList<float[]>();
	}

	/**
	 * Resets the original settings of the ball.
	 * @param direction The direction the ball should take.
	 */
	public void resetBall(double direction){
		setCenterX(contWidth/2f);
		setCenterY((contHeight/2f) + 5f);
		speed = 5.0;
		dx = direction * speed;
		dy = 0.0;
	}

	/**
	 * @return Get the ball
	 */
	public Shape getBall() {
		return ball;
	}

	/************
	 * LOCATION *
	 ************/
	/**
	 * @return Get the X coordinate of the ball
	 */
	public double getX() {
		//		return positionBall[0];
		return ball.getX();
	}

	/**
	 * @return Get the Y coordinate of the ball
	 */
	public double getY() {
		return ball.getY();
	}

	/**
	 * @return Set the X coordinate of the ball
	 */
	public void setX(float x) {
		position[0] = x;
		ball.setX(x);
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
	 * @return Get the center X coordinate
	 */
	public float getCenterX() {
		return ball.getCenterX();
	}

	/**
	 * @return Get the center Y coordinate
	 */
	public float getCenterY() {
		return ball.getCenterY();
	}

	/**
	 * @param x Set the center X coordinate
	 */
	public void setCenterX(float x) {
		ball.setCenterX(x);
	}

	/**
	 * @param y Set the center Y coordinate
	 */
	public void setCenterY(float y) {
		ball.setCenterY(y);
	}

	/**
	 * @return Set the Y coordinate of the ball
	 */
	public void setY(float y) {
		position[1] = y;
		ball.setY(y);
	}
	
	/********
	 * SIZE *
	 ********/
	/**
	 * @return Get the width of the ball
	 */
	public float getWidth(){
		return ball.getWidth();
	}

	/**
	 * @return Get the height of the ball
	 */
	public float getHeight(){
		return ball.getHeight();
	}

	/************
	 * MOVEMENT *
	 ************/
	/**
	 * @return Get the delta X of the ball
	 */
	public double getDx() {
		return dx;
	}

	/**
	 * Set the delta X of the ball
	 * @param dx The difference in Y coordinates
	 */
	public void setDx(double dx) {
		this.dx = dx;
	}

	/**
	 * @return Get the delta Y of the ball
	 */
	public double getDy() {
		return dy;
	}

	/**
	 * Set the delta Y of the ball
	 * @param dy The difference in Y coordinates
	 */
	public void setDy(double dy) {
		this.dy = dy;
	}

	/**
	 * @return Get the Slope (Richtingscoëfficiënt) of the direction of the ball
	 */
	public float getDyDx(){
		return (float)(getDy()) / (float)(getDx());
	}

	/**
	 * @return Get the speed of the ball
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Set the speed of the ball
	 * @param speed The speed
	 */
	public void setBallSpeed(double speed) {
		this.speed = speed;
	}
	
	/***************
	 * MATHEMATICS *
	 ***************/
	/**
	 * Get the X value from a given y point, calculated by simple linear algebra: y=ax+b. Reformulated to x=(y-b)/a
	 * @param direction a = Dy/Dx direction of the line.
	 * @param xPoint X coordinate of a point the line will cross
	 * @param yPoint Y coordinate of a point the line will cross
	 * @param yEnd The Y limit the line will intersect with
	 * @return
	 */
	public float linEqX(float direction, float xPoint, float yPoint, float yEnd) {
		float a = direction;
		float b = ( yPoint - a * xPoint );
		float x = (yEnd - b) / a;
		return x;
	}

	/**
	 * Get the Y value from a given y point, calculated by simple linear algebra: y=ax+b.
	 * @param direction a = Dy/Dx direction of the line.
	 * @param xPoint X coordinate of a point the line will cross
	 * @param yPoint Y coordinate of a point the line will cross
	 * @param xEnd The X limit the line will intersect with
	 * @return
	 */
	public float linEqY(float direction, float xPoint, float yPoint, float xEnd) {
		float a = direction;
		float b = ( yPoint - a * xPoint );
		float y = a * xEnd + b;
		return y;
	}

	/**
	 * Predict the Y coordinate the ball will hit the Player
	 * @param player The player the ball will hit at
	 * @return The predicted Y coordinate.
	 */
	public float predictY(Player player) {
		// start clean
		predictionTrace.clear();
		
		// the offset next to the paddle
		float offsetX = getDx() < 0 ? getWidth() : -getWidth();

		// the offset next to the border
		float offsetY = getHeight()/2;

		// position of the ball
		float x1 = getCenterX();
		float y1 = getCenterY();
		
		// keep track of the bounce coordinates
		predictionTrace.add( new float[]{x1,y1} );

		// position where the ball will end up without bouncing
		float x2 = player.getCenterX() + offsetX;
		float y2 = linEqY(getDyDx(), x1, y1, x2);

		// direction of the ball
		float a = getDyDx();
		// ball up or down
		double dy = getDy();

		// set while counter
		int i = 0;

		// if y2 is out of the frame, it means the ball will bounce
		while((y2 <= 0f || y2 >= contHeight) && ( (getDx() < 0 && getCenterX() > player.getCenterX()) || (getDx() > 0 && getCenterX() < player.getCenterX()) )){

			// which border will it bounce off from
			if( (dy > 0 && i % 2 == 0) || (dy < 0 && i % 2 != 0) )
				y1 = contHeight - offsetY;
			if( (dy < 0 && i % 2 == 0) || (dy > 0 && i % 2 != 0) )
				y1 = 0f + offsetY;

			// change direction depending on the bounce round
			int d = (int) Math.pow(-1, i);

			// calculate hitting point on the border +
			x1 = linEqX(d*a, x2, y2, y1);

			// recalculate estimated hitting point -
			y2 = linEqY(d*-a, x1, y1, x2);
			
			// keep track of the bounce coordinates
			predictionTrace.add( new float[]{x1,y1} );

			// increase counter after prediction
			i++;
		}
		// keep track of the bounce coordinates
		predictionTrace.add( new float[]{x2,y2} );
		return y2;
	}

	/**
	 * Retreive all information about the ball
	 * @return A string representation of the ball
	 */
	public String toString(){
		return "Ball: \n"
				+ "X=" + getCenterX() +"\n"
				+ "Y=" + getCenterY() +"\n"
				+ "Dx=" + getDx() +"\n"
				+ "Dy=" + getDy() +"\n"
				+ "DyDx=" + getDyDx() +"\n"
				+ "b=" + ( getCenterY() - getDyDx() * getCenterX() ) +"\n";
	}
}
