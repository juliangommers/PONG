import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class StartGame extends BasicGame
{
	
	private Shape rect = null;
	private int xP1 = 100;
	private int yP1 = 100;
	
	public StartGame(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setShowFPS(true);
		rect = new Rectangle(xP1,yP1,25,25);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		if( input.isKeyDown(Input.KEY_DOWN)  && rect.getY() <= (container.getHeight()-rect.getHeight()) ){
			yP1++;
			rect.setLocation(xP1, yP1);
		}
		if( input.isKeyDown(Input.KEY_UP) && rect.getY() >= 0 ){
			yP1--;
			rect.setLocation(xP1, yP1);
		}
			
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		
		g.draw(rect);
		g.setColor(new Color(255,255,255));
		g.fill(rect);
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new StartGame("Simple Slick Game"));
			appgc.setDisplayMode(800, 600, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}