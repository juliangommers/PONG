import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Player {
	// set the sizes
	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	private double speed;
	private Shape player;

	private float[] position;

	/**
	 * Constructor to create a new Player. This initialises the Player object.
	 * @param x X coordinate of the Player.
	 * @param y Y coordinate of the Player.
	 */
	public Player(float x, float y){
		position = new float[]{x, y};
		player = new Rectangle(x, y, contWidth/40f, contHeight/3f);
		speed = 5.0;
	}
	
	/**
	 * @return the Player
	 */
	/**
	 * @return
	 */
	public Shape getPlayer() {
		return player;
	}

	/********
	 * SIZE *
	 ********/
	/**
	 * @return The height of the Player.
	 */
	public float getHeight() {
		return player.getHeight();
	}

	/**
	 * @return The width of the Player.
	 */
	public float getWidth() {
		return player.getWidth();
	}

	/************
	 * LOCATION *
	 ************/
	/**
	 * @return Get the X coordinate of the Player.
	 */
	public float getX(){
		return position[0];
	}

	/**
	 * @return Get the Y coordinate of the Player.
	 */
	public float getY(){
		return position[0];
	}

	/**
	 * @param x Set the X coordinate of the Player.
	 */
	public void setX(float x){
		player.setX(x);
		position[0] = x;
	}

	/**
	 * @param y Set the X coordinate of the Player.
	 */
	public void setY(float y){
		player.setY(y);
		position[1] = y;
	}

	/**
	 * @return Get the maximum X coordinate of the Player.
	 */
	public float getMaxX(){
		return player.getMaxX();
	}

	/**
	 * @return Get the minimum X coordinate of the Player.
	 */
	public float getMinX(){
		return player.getMinX();
	}

	/**
	 * @return Get the maximum Y coordinate of the Player.
	 */
	public float getMaxY(){
		return player.getMaxY();
	}

	/**
	 * @return Get the minimum Y coordinate of the Player.
	 */
	public float getMinY(){
		return player.getMinY();
	}

	/**
	 * @return Get the center X coordinate of the Player.
	 */
	public float getCenterX(){
		return player.getCenterX();
	}

	/**
	 * @return Get the center Y coordinate of the Player.
	 */
	public float getCenterY(){
		return player.getCenterY();
	}

	/**
	 * Set the location of the Player.
	 * @param x The X coordinate of the Player.
	 * @param y The Y coordinate of the Player.
	 */
	public void setLocation(float x, float y) {
		player.setLocation(x, y);
	}


	/************
	 * MOVEMENT *
	 ************/
	/**
	 * Move the Player up.
	 */
	public void up(){
		if(player.getMinY() > 0){
			position[1] = position[1] - (float)(speed);
			player.setLocation(position[0], position[1]);
		}
	}

	/**
	 * Move the Player down.
	 */
	public void down(){
		if(player.getMaxY() < contHeight){
			position[1] = position[1] + (float)(speed);
			player.setLocation(position[0], position[1]);
		}
	}

	/**
	 * @return Get the speed of the Player.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed Set the speed of the Player.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public String toString() {
		return "Player: \n"
				+ "X="+ getCenterX() + "\n"
				+ "Y="+ getCenterY() + "\n"
				+ "Height="+ getHeight() + "\n"
				+ "Width="+ getWidth() + "\n"
				+ "Speed="+ getSpeed() + "\n";
	}
}
