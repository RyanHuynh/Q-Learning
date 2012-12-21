import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Vector;


public class Controller extends Thread{
	int delay;
	Agent agent;
	ToolKit toolkit;
	Environment world;
	Policy policy;	
	boolean gameRun, optimalPath;
	int numTrial, totalMove, averageMove, pathLength, prevPathLength;	
	boolean[][] path, prevPath;
	
	public Controller(ToolKit t, Agent a, Environment e, Policy p)
	{
		delay = 200;
		agent = a;
		toolkit = t;
		world = e;
		policy = p;
		gameRun = false;
		optimalPath = true;
		numTrial = 1;
		totalMove = 0;
		averageMove = 0;
		path = new boolean[world.xDim][world.yDim];
		pathLength = 0;
		prevPathLength = Integer.MAX_VALUE;
	}
	public void run()
	{
		try
		{	
			while(true)
			{
				while(gameRun)
				{					
					while(!policy.endState)
					{
						policy.updateState(agent);
						world.updateAgentLoc(agent);
						updatePath();
						SwingUtilities.invokeLater(toolkit);
						sleep(delay);						
					}	
					resetGame();					
				}
				sleep(delay);
			}
		}catch(InterruptedException e){}
	}	
	
	public void resetGame()
	{
		totalMove += agent.numMove;
		agent.relocAgent(toolkit.START_X,toolkit.START_Y);
		agent.nextState();		
		world.paintPath(getPath(), optimalPath);
		averageMove = totalMove/numTrial;
		try
		{
			SwingUtilities.invokeAndWait(toolkit);
		}catch(Exception e){};		
		agent.numMove = 0;
		policy.newReward(0);
		policy.endState  = false;
		numTrial++;
	}
	
	public boolean[][] getPath()
	{		
		if(pathLength < prevPathLength)
		{
			prevPath = path;
			prevPathLength = pathLength;
		}	
		path = new boolean[world.xDim][world.yDim];
		pathLength = 0;
		return prevPath;
	}
	
	public void updatePath()
	{
		if(path[agent.getX()][agent.getY()] == false)
		{
			path[agent.getX()][agent.getY()] = true;
			pathLength++;
		}		
	}
}
