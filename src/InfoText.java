import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;


public class InfoText {

	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	private float[] positionScoreP1 = {contWidth/2-50, 50};
	private float[] positionScoreP2 = {contWidth/2+50, 50};
	
	TrueTypeFont scoreFont;
	TrueTypeFont pauseFont;
	TrueTypeFont pongFont;

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
		scoreFont.drawString(positionScoreP1[0]-10, positionScoreP1[1], scores.getStringScoreP1());
		scoreFont.drawString(positionScoreP2[0]-10, positionScoreP2[1], scores.getStringScoreP2());
	}

	public void pauseScreen(){
		pauseFont.drawString((contWidth/2f)-(pauseFont.getWidth("PAUSE")/2f), (contHeight/2f)-(pauseFont.getHeight()/2f), "PAUSE");
	}

	public void startScreen(){
		pongFont.drawString((contWidth/2f)-(pongFont.getWidth("PONG")/2f), (contHeight/10f)-(pongFont.getHeight()/2f), "PONG");
	}
}
