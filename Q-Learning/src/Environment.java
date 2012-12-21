import java.awt.*;
import javax.swing.JPanel;


public class Environment  extends JPanel{
	
	public int[][] world = {{0,0,0,0,0,1,0,1,0,3},
							{0,0,0,1,0,1,0,1,0,0},
							{0,0,0,1,1,1,0,0,0,0},
							{0,0,0,0,0,0,0,0,1,0},
							{1,1,1,0,0,1,0,0,0,0},
							{0,1,0,0,0,1,0,1,1,0},
							{0,1,0,0,0,0,0,1,0,0},
							{0,0,0,1,1,1,0,1,0,0},
							{0,0,0,0,0,1,0,1,0,0},
							{2,0,0,1,0,0,0,0,0,0}};	
	
	static final int WORLD_WIDTH = 505;
	static final int WORLD_HEIGHT = 505;
	static final int CELL_SIZE = 50;
	Image hole, treasure_img, agent_img;
	Dimension size, worldDim;	
	int xCor, yCor, endX, endY, startX, startY, xDim, yDim;
	boolean[][] path;
	boolean pathOn;
	
	
	
	public Environment(int xDim, int yDim, int startX, int startY, int endX, int endY, Agent a)
	{	
		agent_img = Toolkit.getDefaultToolkit().getImage("3.png");
		xCor = a.getX();
		yCor = a.getY();
		this.endX = endX;
		this.endY = endY;
		this.xDim = xDim;
		this.yDim = yDim;
		this.startX = startX;
		this.startY = startY;
		
		//Panel size
		size = new Dimension(WORLD_WIDTH, WORLD_HEIGHT);
		pathOn = false;
		path = new boolean[xDim][yDim];		
	}
	
	public void paintComponent(Graphics g)
	{
		
		//Draw shortest path
		if(pathOn)
		{
			g.setColor(Color.yellow);		
			for (int i = 0; i < xDim; i++)
			{
				for(int j = 0 ; j < yDim; j++)
				{
					if(path[i][j])
						g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);						
				}
			}
		}
			
		//Draw world.
		for (int i = 0; i < 10; i++)
		{
			g.setColor(Color.black);	
			for(int j = 0 ; j < 10; j++)
			{
				g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				if(world[i][j] == 1)								
					g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);										
			}
		}
		g.setColor(Color.red);
		g.fillRect(endX * CELL_SIZE + 5, endY * CELL_SIZE + 5, 40, 40);
		g.setColor(Color.blue);
		g.fillRect(startX * CELL_SIZE + 5, startY *  CELL_SIZE + 5, 40, 40);				
		
		//Draw agent_img at current position
		g.drawImage(agent_img, xCor *  CELL_SIZE, yCor *  CELL_SIZE, null);
	}
	
	public int[][] getWorldGrid()
	{
		return world;
	}
	public Dimension getPreferredSize() 
	{ 
		return size;
	}
	
	public void updateAgentLoc(Agent a)
	{
		xCor = a.getX();
		yCor = a.getY();
	}	
	
	public void paintPath(boolean[][] newPath, boolean b)
	{
		path = newPath;
		pathOn = b;
	}
}

