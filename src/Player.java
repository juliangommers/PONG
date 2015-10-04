import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Player {

	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	private double playerSpeed = 5;
	private Shape player = null;
	
	private float[] position = {0,0};
	
	/**
	 * @return the player
	 */
	public Shape getPlayer() {
		return player;
	}


	public Player(float x, float y){
		this.position[0] = x;
		this.position[1] = y;
		player = new Rectangle(x, y, contWidth/40f, contHeight/3f);
	}
	
	public void up(){
		position[1] = position[1] - (float)(playerSpeed);
		player.setLocation(this.position[0], this.position[1]);
	}
	
	public void down(){
		position[1] = position[1] + (float)(playerSpeed);
		player.setLocation(this.position[0], this.position[1]);
	}

	public float getHeight() {
		return player.getHeight();
	}

	public float getWidth() {
		return player.getWidth();
	}

	public void setLocation(float x, float y) {
		player.setLocation(x, y);
	}

	public float getX(){
		return position[0];
	}

	public float getY(){
		return position[0];
	}
	
	public void setX(float x){
		player.setX(x);
		this.position[0] = x;
	}

	public void setY(float y){
		player.setY(y);
		this.position[1] = y;
	}
	
	public float getMaxX(){
		return player.getMaxX();
	}
	
	public float getMinX(){
		return player.getMinX();
	}
	
	public float getMaxY(){
		return player.getMaxY();
	}
	
	public float getMinY(){
		return player.getMinY();
	}

	/**
	 * @return the playerSpeed
	 */
	public double getPlayerSpeed() {
		return playerSpeed;
	}

	/**
	 * @param playerSpeed the playerSpeed to set
	 */
	public void setPlayerSpeed(double playerSpeed) {
		this.playerSpeed = playerSpeed;
	}
}
