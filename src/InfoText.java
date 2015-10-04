import org.newdawn.slick.TrueTypeFont;


public class InfoText {

	private static int contHeight = StartGame.contHeight;
	private static int contWidth = StartGame.contWidth;

	private float[] positionScoreP1 = {contWidth/2-50, 50};
	private float[] positionScoreP2 = {contWidth/2+50, 50};
	
	TrueTypeFont scoreFont;
	TrueTypeFont pauseFont;

	public InfoText(){

	}

	public void scores(Scores scores){
		scoreFont.drawString(positionScoreP1[0]-10, positionScoreP1[1], scores.getStringScoreP1());
		scoreFont.drawString(positionScoreP2[0]-10, positionScoreP2[1], scores.getStringScoreP2());
	}

	public void pauseScreen(){
		pauseFont.drawString((contWidth/2f)-(pauseFont.getWidth("PAUSE")/2f), (contHeight/2f)-(pauseFont.getHeight()/2f), "PAUSE");
	}

	public void startScreen(){

	}
}
