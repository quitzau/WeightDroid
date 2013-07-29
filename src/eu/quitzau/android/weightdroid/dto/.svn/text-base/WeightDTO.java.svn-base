package eu.quitzau.android.weightdroid.dto;

import java.io.Serializable;
import java.util.Date;

public class WeightDTO implements Comparable<WeightDTO>, Serializable {
	
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private float weight;
	private float size;
	private Date date;
	
	public WeightDTO()
	{
		
	}
	
	public WeightDTO(float weight, float size, Date date)
	{
		this.date = date;
		this.size = size;
		this.weight = weight;
	}
	
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(WeightDTO another) {
		int dateCmp = this.date.compareTo(another.getDate());
        return dateCmp;
	}

}
