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

    // used fonts
    TrueTypeFont scoreFont;
    TrueTypeFont pauseFont;
    TrueTypeFont pongFont;
    TrueTypeFont playerFont;
    TrueTypeFont levelFont;
    TrueTypeFont bounceFont;
    TrueTypeFont predictionFont;

    // do you want predictions?
    Boolean prediction;
    Boolean predictionTraces;

    /**
     *
     */
	public InfoText(){
		positionScoreP1 = new float[]{(contWidth/10f)*4f, 50};
		positionScoreP2 = new float[]{(contWidth/10f)*6f, 50};
        prediction = false;
        predictionTraces = false;
    }

    /**
     * @param g
     */
	public void dashedLine(Graphics g){
		for (int i = 0; i < contHeight; i += contWidth/30f) {
			g.drawLine(contWidth/2f, i, contWidth/2f, (float)(i + contWidth/60f));
        }
    }

    /**
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
     * @param scores
     */
	public void scores(Scores scores){
		scoreFont.drawString(positionScoreP1[0]-scoreFont.getWidth(scores.getStringScoreP1()), positionScoreP1[1], scores.getStringScoreP1());
        scoreFont.drawString(positionScoreP2[0], positionScoreP2[1], scores.getStringScoreP2());
    }

    /**
     *
     */
    public void pauseScreen() {
        pauseFont.drawString((contWidth / 2f) - (pauseFont.getWidth("PAUSE") / 2f), (contHeight / 2f) - (pauseFont.getHeight() / 2f), "PAUSE");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("P- resume") / 2f), (contHeight / 10f) * 7 - (scoreFont.getHeight() / 2f), "P- resume");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("R- return to start screen") / 2f), (contHeight / 10f) * 8 - (scoreFont.getHeight() / 2f), "R- return to start screen");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("ESC- exit game") / 2f), (contHeight / 10f) * 9 - (scoreFont.getHeight() / 2f), "ESC- exit game");
    }

    /**
     *
     */
    public void startScreen() {
        pongFont.drawString((contWidth / 2f) - (pongFont.getWidth("PONG") / 2f), (contHeight / 10f) - (pongFont.getHeight() / 2f), "PONG");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("1- SINGLE PLAYER") / 2f), (contHeight / 10f) * 3 - (scoreFont.getHeight() / 2f), "1- SINGLE PLAYER");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("2- MULTIPLAYER") / 2f), (contHeight / 10f) * 4 - (scoreFont.getHeight() / 2f), "2- MULTIPLAYER");
        scoreFont.drawString((contWidth / 2f) - (scoreFont.getWidth("3- INSANE 2P") / 2f), (contHeight / 10f) * 5 - (scoreFont.getHeight() / 2f), "3- INSANE 2P");
    }

    /**
     *
     */
    public void playerInstructions() {
        playerFont.drawString((contWidth / 20f), (contHeight / 20f) * 17 - (playerFont.getHeight() / 2f), "P1");
        playerFont.drawString((contWidth / 20f), (contHeight / 20f) * 18 - (playerFont.getHeight() / 2f), "W - up");
        playerFont.drawString((contWidth / 20f), (contHeight / 20f) * 19 - (playerFont.getHeight() / 2f), "S - down");
        playerFont.drawString((contWidth / 20f) * 10 - (playerFont.getWidth("F- Full screen") / 2f), (contHeight / 20f) * 17 - (playerFont.getHeight() / 2f), "F- Full screen");
        playerFont.drawString((contWidth / 20f) * 10 - (playerFont.getWidth("8/9- Enable predictions") / 2f), (contHeight / 20f) * 18 - (playerFont.getHeight() / 2f), "8/9- Enable predictions");
        playerFont.drawString((contWidth / 20f) * 10 - (playerFont.getWidth("0- Show FPS") / 2f), (contHeight / 20f) * 19 - (playerFont.getHeight() / 2f), "0- Show FPS");
        playerFont.drawString(((contWidth / 20f) * 19) - playerFont.getWidth("P2"), (contHeight / 20f) * 17 - (playerFont.getHeight() / 2f), "P2");
        playerFont.drawString(((contWidth / 20f) * 19) - playerFont.getWidth("UP - up"), (contHeight / 20f) * 18 - (playerFont.getHeight() / 2f), "UP - up");
        playerFont.drawString(((contWidth / 20f) * 19) - playerFont.getWidth("DOWN - down"), (contHeight / 20f) * 19 - (playerFont.getHeight() / 2f), "DOWN - down");
    }

    /**
     *
     */
    public void levelScreen() {
        levelFont.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 3 - (levelFont.getHeight() / 2f), "Choose difficulty");
        levelFont.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 4 - (levelFont.getHeight() / 2f), "1 - Beginner");
        levelFont.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 5 - (levelFont.getHeight() / 2f), "2 - Intermediate");
        levelFont.drawString((contWidth / 10f) * 3, (contHeight / 10f) * 6 - (levelFont.getHeight() / 2f), "3 - Expert");
    }

    /**
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
                    predictionFont.drawString(x2 - (predictionFont.getWidth("[]") / 2f), y2 - (predictionFont.getHeight() / 2f), "[]");
                } else {
                    if (predictionTraces) {
                        bounceFont.drawString(x2 - (bounceFont.getWidth("x") / 2f), y2 - (bounceFont.getHeight() / 2f), "x");
                    }
                }
            }
        }
    }
}
