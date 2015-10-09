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

	private float[] positionScoreP1 = {contWidth/2, 50};
	private float[] positionScoreP2 = {contWidth/2, 50};

	TrueTypeFont scoreFont;
	TrueTypeFont pauseFont;
	TrueTypeFont pongFont;
	TrueTypeFont playerFont;

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

	public void predictY(Player player, Ball ball) {
		float displayX = player.getCenterX();
		float displayY = ball.predictY(player);
//		while(displayY < 0 || displayY > contHeight){
		for(int i = 0; (displayY < 0 || displayY > contHeight) && i < 5; i++){
			float bounceX0 = ball.linIntersectX(0f);
			float bounceXH = ball.linIntersectX(contHeight);
			if(displayY < 0){
				displayY = ball.linEq(bounceX0, 0, displayX);
			}
			if(displayY > contHeight){
				displayY = ball.linEq(bounceXH, contHeight, displayX);
			}
//			Haal deze comment code weg en je ziet hoe vaak de loop door gaat
//			scoreFont.drawString((contWidth/2f)-(scoreFont.getWidth(Integer.toString(i))/2f) , (contHeight/10f)*i-(scoreFont.getHeight()/2f), Integer.toString(i));
		}
		scoreFont.drawString(displayX-(scoreFont.getWidth("X")/2f) , displayY-(scoreFont.getHeight()/2f), "X");
	}
}
