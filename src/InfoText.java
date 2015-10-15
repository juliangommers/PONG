import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;


public class InfoText {

	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	private float[] positionScoreP1 = {contWidth/2, 50};
	private float[] positionScoreP2 = {contWidth/2, 50};

	TrueTypeFont scoreFont;
	TrueTypeFont pauseFont;
	TrueTypeFont pongFont;
	TrueTypeFont playerFont;
	TrueTypeFont levelFont;
	TrueTypeFont bounceFont;
	TrueTypeFont predictionFont;

	Boolean prediction = false;
	Boolean predictionTraces = false;

	public InfoText(){
	}

	public Font loadFont(String location, int style, int size){
		try {
			InputStream fontUrl = new FileInputStream(new File(location));
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontUrl);
			font = font.deriveFont(style, size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			return font;
		} catch (FontFormatException e) {
			System.out.println("Something went wrong with formatting the font!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("The file could not be read.");
			e.printStackTrace();
		}
		return null;
	}

	public void scores(Scores scores){
		scoreFont.drawString(positionScoreP1[0]-50-scoreFont.getWidth(scores.getStringScoreP1()), positionScoreP1[1], scores.getStringScoreP1());
		scoreFont.drawString(positionScoreP2[0]+50, positionScoreP2[1], scores.getStringScoreP2());
	}

	public void pauseScreen(){
		pauseFont.drawString((contWidth/2f)-(pauseFont.getWidth("PAUSE")/2f), (contHeight/2f)-(pauseFont.getHeight()/2f), "PAUSE");
		scoreFont.drawString((contWidth/2f)-(scoreFont.getWidth("R- restart")/2f), (contHeight/10f)*9-(scoreFont.getHeight()/2f), "R- restart");
	}

	public void startScreen(){
		pongFont.drawString((contWidth/2f)-(pongFont.getWidth("PONG")/2f), (contHeight/10f)-(pongFont.getHeight()/2f), "PONG");
		scoreFont.drawString((contWidth/2f)-(scoreFont.getWidth("1- SINGLE PLAYER")/2f), (contHeight/10f)*5-(scoreFont.getHeight()/2f), "1- SINGLE PLAYER");
		scoreFont.drawString((contWidth/2f)-(scoreFont.getWidth("2- MULTIPLAYER")/2f), (contHeight/10f)*6-(scoreFont.getHeight()/2f), "2- MULTIPLAYER");
		scoreFont.drawString((contWidth/2f)-(scoreFont.getWidth("3- INSANE 2P")/2f), (contHeight/10f)*7-(scoreFont.getHeight()/2f), "3- INSANE 2P");
		playerFont.drawString((contWidth/20f), (contHeight/20f)*17-(playerFont.getHeight()/2f), "P1");
		playerFont.drawString((contWidth/20f), (contHeight/20f)*18-(playerFont.getHeight()/2f), "W - up");
		playerFont.drawString((contWidth/20f), (contHeight/20f)*19-(playerFont.getHeight()/2f), "S - down");
		playerFont.drawString(((contWidth/20f)*19)-playerFont.getWidth("P2"), (contHeight/20f)*17-(playerFont.getHeight()/2f), "P2");
		playerFont.drawString(((contWidth/20f)*19)-playerFont.getWidth("UP - up"), (contHeight/20f)*18-(playerFont.getHeight()/2f), "UP - up");
		playerFont.drawString(((contWidth/20f)*19)-playerFont.getWidth("DOWN - down"), (contHeight/20f)*19-(playerFont.getHeight()/2f), "DOWN - down");
	}
	
	public void levelScreen(){
		levelFont.drawString((contWidth/10f)*3, (contHeight/10f)*4-(levelFont.getHeight()/2f), "1 - Beginner");
		levelFont.drawString((contWidth/10f)*3, (contHeight/10f)*5-(levelFont.getHeight()/2f), "2 - Intermediate");
		levelFont.drawString((contWidth/10f)*3, (contHeight/10f)*6-(levelFont.getHeight()/2f), "3 - Expert");
	}

	public void predictY(Player player, Ball ball, Graphics g) {
		// the offset next to the paddle
		float offsetX = ball.getBallDx() < 0 ? ball.getWidth() : -ball.getWidth();

		// the offset next to the border
		float offsetY = ball.getHeight()/2;

		// position of the ball
		float x1 = ball.getCenterX();
		float y1 = ball.getCenterY();

		// position where the ball will end up without bouncing
		float x2 = player.getCenterX() + offsetX;
		float y2 = ball.linEqY(ball.getDyDx(), x1, y1, x2);

		// direction of the ball
		float a = ball.getDyDx();
		// ball up or down
		double dy = ball.getBallDy();

		// set while counter
		int i = 0;

		// set the colour to gray
		g.setColor(new Color(127, 127, 127));

		// if y2 is out of the frame, it means the ball will bounce
		while((y2 <= 0f || y2 >= contHeight)){
			
			// show ball traces
			if(this.predictionTraces)
				g.drawLine(x1, y1, x2, y2);
			
			// which border will it bounce off from
			if( (dy > 0 && i % 2 == 0) || (dy < 0 && i % 2 != 0) )
				y1 = contHeight - offsetY;
			if( (dy < 0 && i % 2 == 0) || (dy > 0 && i % 2 != 0) )
				y1 = 0f + offsetY;

			// change direction depending on the bounce round
			int d = (int) Math.pow(-1, i);

			// calculate hitting point on the border +
			x1 = ball.linEqX(d*a, x2, y2, y1);

			// recalculate estimated hitting point -
			y2 = ball.linEqY(d*-a, x1, y1, x2);

			// show where the ball will hit on the border
			if(this.predictionTraces)
				bounceFont.drawString(x1-(bounceFont.getWidth("x")/2f) , y1-(bounceFont.getHeight()/2f), "x");

			// increase counter after prediction
			i++;
		}
		// show ball traces
		if(this.predictionTraces)
			g.drawLine(x1, y1, x2, y2);

		// set the colour to white
		g.setColor(new Color(225, 225, 225));
		
		// print the prediction
		predictionFont.drawString(x2-(predictionFont.getWidth("[]")/2f) , y2-(predictionFont.getHeight()/2f), "[]");
	}
}
