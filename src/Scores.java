
public class Scores {

	// Let's start the game at 0 - 0
	private int[] scores;
	
	/**
	 * Constructor to initialise the Socres.
	 */
	public Scores(){
		scores = new int[]{0,0};
	}
	
	/**
	 * @return Get the score of Player 1
	 */
	public int getScoreP1() {
		return scores[0];
	}
	
	/**
	 * @return  Get the score of Player 1
	 */
	public int getScoreP2() {
		return scores[1];
	}
	
	/**
	 * @return Get the Scores
	 */
	public int[] getScors() {
		return scores;
	}
	
	/**
	 * @param Set the Scores
	 */
	public void setScores(int[] scores) {
		this.scores = scores;
	}
	
	/**
	 * @return Get the string score of Player 1
	 */
	public String getStringScoreP1() {
		return Integer.toString(scores[0]);
	}
	
	/**
	 * @return  Get the string score of Player 2
	 */
	public String getStringScoreP2() {
		return Integer.toString(scores[1]);
	}
	
	/**
	 * @return Increase the score of Player 1
	 */
	public void increaseScoreP1() {
		scores[0]++;
	}
	
	/**
	 * @return Increase the score of Player 2
	 */
	public void increaseScoreP2() {
		scores[1]++;
	}

	public String toString() {
		return "Scores [getStringScoreP1()=" + getStringScoreP1()
				+ ", getStringScoreP2()=" + getStringScoreP2() + "]";
	}
}
