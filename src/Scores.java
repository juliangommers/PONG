
public class Scores {

	// Let's start the game at 0 - 0
	private int[] scores = {0,0};
	
	/**
	 * @return the scores
	 */
	public int[] getScors() {
		return scores;
	}
	
	/**
	 * @return the scores
	 */
	public int getScoreP1() {
		return scores[0];
	}
	
	/**
	 * @return the scores
	 */
	public int getScoreP2() {
		return scores[1];
	}
	
	/**
	 * @return the scores
	 */
	public String getStringScoreP1() {
		return Integer.toString(scores[0]);
	}
	
	/**
	 * @return the scores
	 */
	public String getStringScoreP2() {
		return Integer.toString(scores[1]);
	}
	
	/**
	 * @return the scores
	 */
	public void increaseScoreP1() {
		scores[0]++;
	}
	
	/**
	 * @return the scores
	 */
	public void increaseScoreP2() {
		scores[1]++;
	}
}
