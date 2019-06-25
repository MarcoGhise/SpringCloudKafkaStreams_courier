package it.blog.springcloudstream.bean;

public class Gps {

	private String position;	
	private String destination;
	
	public Gps(String destination, String position)
	{
		this.setDestination(destination); 
		this.setPosition(position);
		
		System.out.println(destination + " - " + position);
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Gps [position=" + position + ", destination=" + destination + "]";
	}
}
