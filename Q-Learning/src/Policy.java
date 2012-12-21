public class Policy {

	private Agent agent;
	private Environment world;
	private Double[][][] qTable;
	private int xDim, yDim;
	private double reward;
	private final static int NUM_ACTIONS = 4;
	private final static double INIT_QVALUE = 0;	
	private final static double EPSILON = 0.1;
	private final static double GAMMA = 0.99;
	private final static double ALPHA = 0.9;
	private final static double END_REWARD = 50;
	public boolean endState;
	
	public Policy(Agent a, Environment e)
	{		
		agent = a;
		world = e;
		xDim = e.xDim;
		yDim = e.yDim;
		qTable = new Double[xDim][yDim][NUM_ACTIONS];
		initQvalue(INIT_QVALUE);
		reward = 0;
		endState  = false;
	}
	
	public void initQvalue(double initValue)
	{
		for(int i = 0; i < xDim; i++)
		{
			for(int j = 0; j < yDim; j++)
			{
				for(int k = 0; k < NUM_ACTIONS; k++)
				{
					qTable[i][j][k] = initValue;
				}
			}
		}
	}
	
	//Action choice using e-greedy method.
	public int getBestAction(boolean[] b, int[] state)
	{
		int actionChoice = -1;
		if(Math.random() < EPSILON)
		{
			while(actionChoice == -1)
			{
				int random = (int) (Math.random() * b.length);
				if(b[random])
					actionChoice = random;
			}
		}
		else 
		{
			double maxQ = -1.0;
			int[] temp  = new int[b.length];
			int index = 0;
			for(int i = 0; i < b.length; i++)
			{
				if(b[i])
				{
					if(getQValue(state, i) > maxQ)
					{
						maxQ = getQValue(state, i);
						actionChoice = i;
						temp[0] = i;
						index = 0;
					}
					else if(getQValue(state, i) == maxQ)
					{
						index++;
						temp[index] = i;
					}
				}
			}
			if(index > 0)
			{
				int random = (int) (Math.random() * (index + 1));
				actionChoice = temp[random];
			}
		}
		return actionChoice;
	}	
	
	public void updateState(Agent a)
	{
		int[] currentState = {a.getX(), a.getY()};
		boolean[] avaiActions = getAvailableActions();
		int bestAction = getBestAction(avaiActions, a.currentState());
		a.move(bestAction);
		checkEndState(a);
		updateQValue(currentState, bestAction, a);
	}
	
	public void updateQValue(int[] state, int action, Agent a)
	{
		double currentQvalue = qTable[state[0]][state[1]][action];
		boolean[] avaiActions = getAvailableActions();
		double maxQ = -1.0;
		for(int i = 0; i < avaiActions.length; i++)
		{
			if(avaiActions[i])
			{
				if(getQValue(a.currentState(), i) > maxQ)
					maxQ = getQValue(a.currentState(), i);					
			}
		}
		qTable[state[0]][state[1]][action] = currentQvalue + ALPHA * (reward  + GAMMA * maxQ - currentQvalue);		
	}
	public double getQValue(int[] state, int action)
	{
		double value;
		value = qTable[state[0]][state[1]][action];
		return value;
	}
	public void checkEndState(Agent a)
	{
		if(a.getX() == world.endX && a.getY() == world.endY)
		{		
			endState = true;
			newReward(END_REWARD);
		}
	}
	
	public void newReward(double newReward)
	{
		reward = newReward;
	}
	
	public boolean[] getAvailableActions()
	{
		int prevAction = agent.prevAction;
		boolean[] actions = new boolean[NUM_ACTIONS];
		int counter = 0;
		while(counter == 0)
		{
			if(isWall(0) || prevAction == 1)
				actions[0]= false;
			else
			{
				counter++;
				actions[0]= true;
			}
			
			if(isWall(1) || prevAction == 0)
				actions[1] = false;
			else
			{
				counter++;
				actions[1] = true;
			}
			
			if(isWall(2) || prevAction == 3)
				actions[2] = false;
			else
			{
				counter++;
				actions[2] = true;
			}
			
			if(isWall(3) || prevAction == 2)
				actions[3] = false;
			else
			{
				counter++;
				actions[3] = true;
			}
			if(counter == 0)
				prevAction = -1; 
		}
		return actions;
	}
	public boolean isWall(int action)
	{
		int y = agent.getY();
		int x= agent.getX();
		
		if(action == 0)
		{
			int tempY = y - 1;
			if(tempY < 0)
				return true;
			else if(world.getWorldGrid()[x][tempY] == 1)
				return true;
		}
		if(action == 1)
		{
			int tempY = y + 1;
			if(tempY > 9)
				return true;
			else if(world.getWorldGrid()[x][tempY] == 1)
				return true;
		}
		if(action == 2)
		{
			int tempX = x - 1;
			if(tempX < 0)
				return true;
			else if(world.getWorldGrid()[tempX][y] == 1)
				return true;
		}
		if(action == 3)
		{
			int tempX = x + 1;
			if(tempX > 9)
				return true;
			else if(world.getWorldGrid()[tempX][y] == 1)
				return true;
		}
		return false;
	}
}
