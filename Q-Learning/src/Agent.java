import java.awt.Image;


public class Agent {	

	final static int UP = 0;
	final static int DOWN = 1;
	final static int LEFT = 2;
	final static int RIGHT = 3;
	private int currentX, currentY;
	Image agent_img;
	int numMove, prevAction;	
	int[] state;
	
	
	
	public Agent(int startX, int startY)
	{
		currentX = startX;
		currentY = startY;
		state = new int[2];
		nextState();
		numMove = 0;
		prevAction = -1;
	}
		
	public int getX()
	{
		return currentX;
	}
	
	public int getY()
	{
		return currentY;
	}
	
	
	public void move(int action)
	{
		switch(action)
		{
			case UP:
				currentY--;	
				break;
			case DOWN:
				currentY++;
				break;
			case LEFT:
				currentX--;
				break;
			case RIGHT:
				currentX++;
				break;
		}
		nextState();
		numMove++;
		prevAction = action;
	}		
	
	
	public void nextState()
	{
		state[0] = currentX;
		state[1] = currentY;
	}
	public int[] currentState()
	{
		return state;
	}
	public void relocAgent(int x, int y)
	{
		currentX = x;
		currentY = y;
	}
	
}
