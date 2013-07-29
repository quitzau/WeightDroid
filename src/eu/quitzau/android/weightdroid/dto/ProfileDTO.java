package eu.quitzau.android.weightdroid.dto;

public class ProfileDTO {

	public ProfileDTO() {
	
	}
	
	public ProfileDTO(String user, int height, float weightGoal)
	{
		this.height = height;
		this.user = user;
		this.weightGoal = weightGoal;
	}
	
	private String user;
	private int height;
	private float weightGoal;
	
	public float getWeightGoal() {
		return weightGoal;
	}

	public void setWeightGoal(float weightGoal) {
		this.weightGoal = weightGoal;
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
