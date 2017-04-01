package it.randomtower;

public class Controller {

	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;

	public boolean lastLeft;
	public boolean lastRight;
	public boolean lastUp;
	public boolean lastDown;

	public void clearLast() {
		lastLeft = false;
		lastRight = false;
		lastUp = false;
		lastDown = false;

	}

}
