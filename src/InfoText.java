import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;


public class InfoText {
	// set the sizes
	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	// locations of the scores
	private float[] positionScoreP1;
	private float[] positionScoreP2;

	// used fonts (True Type Font (ttf) + font size + bold)
	TrueTypeFont ttf15b;
	TrueTypeFont ttf20b;
	TrueTypeFont ttf30b;
	TrueTypeFont ttf50b;

	// do you want predictions?
	Boolean prediction;
	Boolean predictionTraces;
	
	/**
	 * Constructor
	 */
	public InfoText(){
		positionScoreP1 = new float[]{(contWidth/10f)*4f, 50};
		positionScoreP2 = new float[]{(contWidth/10f)*6f, 50};
		prediction = false;
		predictionTraces = false;
	}

	/**
	 * Dashed line of the game representing the middle.
	 * @param g The field
	 */
	public void dashedLine(Graphics g){
		for (int i = 0; i < contHeight; i += contWidth/30f) {
			g.drawLine(contWidth/2f, i, contWidth/2f, (float)(i + contWidth/60f));
		}
	}

	/**
	 * Import fonts.
	 * @param location 
	 * @param style
	 * @param size
	 * @return
	 */
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

	/**
	 * Show scores
	 * @param scores
	 */
	public void scores(Scores scores){
		ttf50b.drawString(positionScoreP1[0]-ttf50b.getWidth(scores.getStringScoreP1()), positionScoreP1[1], scores.getStringScoreP1());
		ttf50b.drawString(positionScoreP2[0], positionScoreP2[1], scores.getStringScoreP2());
	}

	/**
	 * Pause screen
	 */
	public void pauseScreen() {
		ttf50b.drawString((contWidth / 2f) - (ttf50b.getWidth("PAUSE") / 2f), (contHeight / 2f) - (ttf50b.getHeight() / 2f), "PAUSE");
		ttf50b.drawString((contWidth / 2f) - (ttf50b.getWidth("P- resume") / 2f), (contHeight / 10f) * 7 - (ttf50b.getHeight() / 2f), "P- resume");
		ttf50b.drawString((contWidth / 2f) - (ttf50b.getWidth("R- return to start screen") / 2f), (contHeight / 10f) * 8 - (ttf50b.getHeight() / 2f), "R- return to start screen");
		ttf50b.drawString((contWidth / 2f) - (ttf50b.getWidth("ESC- exit game") / 2f), (contHeight / 10f) * 9 - (ttf50b.getHeight() / 2f), "ESC- exit game");
	}

	/**
	 * Starting screen
	 */
	public void startScreen() {
		ttf50b.drawString((contWidth / 2f) - (ttf50b.getWidth("PONG") / 2f), (contHeight / 10f) - (ttf50b.getHeight() / 2f), "PONG");
		ttf30b.drawString((contWidth / 2f) - (ttf30b.getWidth("1- SINGLE PLAYER") / 2f), (contHeight / 10f) * 3 - (ttf30b.getHeight() / 2f), "1- SINGLE PLAYER");
		ttf30b.drawString((contWidth / 2f) - (ttf30b.getWidth("2- MULTIPLAYER") / 2f), (contHeight / 10f) * 4 - (ttf30b.getHeight() / 2f), "2- MULTIPLAYER");
		ttf30b.drawString((contWidth / 2f) - (ttf30b.getWidth("3- INSANE 2P") / 2f), (contHeight / 10f) * 5 - (ttf30b.getHeight() / 2f), "3- INSANE 2P");
	}

	/**
	 * Instructions how to play
	 */
	public void playerInstructions() {
		ttf20b.drawString((contWidth / 20f), (contHeight / 20f) * 17 - (ttf20b.getHeight() / 2f), "P1");
		ttf20b.drawString((contWidth / 20f), (contHeight / 20f) * 18 - (ttf20b.getHeight() / 2f), "W - up");
		ttf20b.drawString((contWidth / 20f), (contHeight / 20f) * 19 - (ttf20b.getHeight() / 2f), "S - down");
		ttf20b.drawString((contWidth / 20f) * 10 - (ttf20b.getWidth("F- Full screen") / 2f), (contHeight / 20f) * 17 - (ttf20b.getHeight() / 2f), "F- Full screen");
		ttf20b.drawString((contWidth / 20f) * 10 - (ttf20b.getWidth("8/9- Enable predictions") / 2f), (contHeight / 20f) * 18 - (ttf20b.getHeight() / 2f), "8/9- Enable predictions");
		ttf20b.drawString((contWidth / 20f) * 10 - (ttf20b.getWidth("0- Show FPS") / 2f), (contHeight / 20f) * 19 - (ttf20b.getHeight() / 2f), "0- Show FPS");
		ttf20b.drawString(((contWidth / 20f) * 19) - ttf20b.getWidth("P2"), (contHeight / 20f) * 17 - (ttf20b.getHeight() / 2f), "P2");
		ttf20b.drawString(((contWidth / 20f) * 19) - ttf20b.getWidth("UP - up"), (contHeight / 20f) * 18 - (ttf20b.getHeight() / 2f), "UP - up");
		ttf20b.drawString(((contWidth / 20f) * 19) - ttf20b.getWidth("DOWN - down"), (contHeight / 20f) * 19 - (ttf20b.getHeight() / 2f), "DOWN - down");
	}

	/**
	 * Choose difficulty of the level
	 */
	public void levelScreen() {
		ttf30b.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 3 - (ttf30b.getHeight() / 2f), "Choose difficulty");
		ttf30b.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 4 - (ttf30b.getHeight() / 2f), "1 - Beginner");
		ttf30b.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 5 - (ttf30b.getHeight() / 2f), "2 - Intermediate");
		ttf30b.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 6 - (ttf30b.getHeight() / 2f), "3 - Expert");
	}
	
	/**
	 * Show the credits
	 */
	public void credits(){
		String str1 = "Thank you for playing PONG!";
		String str2 = "Original game by: Allan Alcorn (Atari Inc.)";
		String str3 = "Game reproduced by:";
		String str4 = "Julian Gommers & Sjoerd Furth";
		String str5 = "(window closes automatically)";
		ttf30b.drawString((contWidth / 2f) - (ttf30b.getWidth(str1) / 2f), (contHeight / 10f) * 3 - (ttf30b.getHeight() / 2f), str1);
		ttf20b.drawString((contWidth / 2f) - (ttf20b.getWidth(str2) / 2f), (contHeight / 10f) * 4 - (ttf20b.getHeight() / 2f), str2);
		ttf30b.drawString((contWidth / 2f) - (ttf30b.getWidth(str3) / 2f), (contHeight / 10f) * 5 - (ttf30b.getHeight() / 2f), str3);
		ttf20b.drawString((contWidth / 2f) - (ttf20b.getWidth(str4) / 2f), (contHeight / 10f) * 6 - (ttf20b.getHeight() / 2f), str4);
		ttf15b.drawString((contWidth / 2f) - (ttf15b.getWidth(str5) / 2f), (contHeight / 10f) * 9 - (ttf15b.getHeight() / 2f), str5);
	}

	/**
	 * Predict the predicted Y
	 * @param ball
	 * @param g
	 */
	public void predictY(Ball ball, Graphics g) {
		for (int i = 1; i < ball.predictionTrace.size(); i++) {

			float x1 = ball.predictionTrace.get(i - 1)[0];
			float y1 = ball.predictionTrace.get(i - 1)[1];
			float x2 = ball.predictionTrace.get(i)[0];
			float y2 = ball.predictionTrace.get(i)[1];

			// show ball traces
			if (predictionTraces) {
				g.drawLine(x1, y1, x2, y2);
			}

			// show where the ball will hit on the border
			if (predictionTraces || prediction) {
				if (i == ball.predictionTrace.size() - 1) {
					ttf50b.drawString(x2 - (ttf50b.getWidth("[]") / 2f), y2 - (ttf50b.getHeight() / 2f), "[]");
				} else {
					if (predictionTraces) {
						ttf20b.drawString(x2 - (ttf20b.getWidth("x") / 2f), y2 - (ttf20b.getHeight() / 2f), "x");
					}
				}
			}
		}
	}
}
