import java.awt.*;
import java.awt.event.*;



import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;


public class ToolKit extends JApplet implements Runnable, ActionListener{

	//Define Global Variable
	Environment world;
	Agent agent;
	Policy policy;
	Controller control;
	
	Container mainPanel;
	JLabel numMove, numTrial, avgMove;
	Image agent_img;
	Thread runner;		
	
	final static int DEFAULT_X_DIM = 10;
	final static int DEFAULT_Y_DIM = 10;
	final static int START_X = 0;
	final static int START_Y = 9;
	final static int END_X = 9;
	final static int END_Y = 0;
	
	
	public void init()
	{
		this.resize(900,550);
		agent = new Agent(START_X, START_Y);
		world = new Environment(DEFAULT_X_DIM,DEFAULT_Y_DIM, START_X, START_Y, END_X, END_Y, agent);
		policy = new Policy(agent, world);
		control = new Controller(this, agent, world, policy);
		
		
		
		mainPanel = makeMainPanel();
		getContentPane().add(mainPanel);
		
		control.start();
	}
		
	Container makeMainPanel()
	{
		JPanel thisPanel = new JPanel();		
		
		thisPanel.add(makeWorldPanel());
		//thisPanel.add(Box.createHorizontalGlue());
		thisPanel.add(makeSettingPanel());				
		return thisPanel;			
	}
	
	Container makeSettingPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		
		thisPanel.add(makeControlPanel());		
		thisPanel.add(makeStatPanel());
		return thisPanel;
	}
	
	Container makeWorldPanel()
	{		
		JPanel worldPanel = new JPanel();
		worldPanel.add(world);		
		return worldPanel;
	}
	
	Container makeControlPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.setBorder(BorderFactory.createTitledBorder("Game Control"));	
		thisPanel.setPreferredSize(new Dimension(300,200));
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);	
		thisPanel.add(makeButtonPanel());
		thisPanel.add(makeSpeedButton());
		thisPanel.add(makePathOption());
		return thisPanel;
	}
	
	Container makeButtonPanel()
	{
		JPanel thisPanel = new JPanel();				
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JButton startB = new JButton("Start");
		final JButton stopB = new JButton("Stop");	
		startB.setActionCommand("START");
		stopB.setActionCommand("STOP");
		
		
		startB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				control.gameRun = true;
				startB.setEnabled(false);
				stopB.setEnabled(true);
			}
		});
		thisPanel.add(startB);
		
		
		stopB.setEnabled(false);
		stopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.gameRun = false;
				startB.setEnabled(true);
				stopB.setEnabled(false);
			}
		});
		thisPanel.add(stopB);	
		
		JButton resetB = new JButton("Reset Score");		
		resetB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				control.averageMove = 0;
				control.numTrial = 1;
				control.totalMove = 0;
			}
		});
		thisPanel.add(resetB);
		
		return thisPanel;
	}
	
	Container makePathOption()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JCheckBox cb = new JCheckBox("Display optimal path", true);		
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					control.optimalPath = false;
				else control.optimalPath = true;
			}
		});
		thisPanel.add(cb);
		return thisPanel;
	}
	
	Container makeSpeedButton()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel label= new JLabel("Speed: ");
		JRadioButton slow = new JRadioButton("Slow", true);
		slow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				control.delay = 200;
			}
		});
		
		JRadioButton fast = new JRadioButton("Fast");
		fast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				control.delay = 20;
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(slow);
		bg.add(fast);
		thisPanel.add(label);
		thisPanel.add(slow);
		thisPanel.add(fast);
		return thisPanel;
	}
	
	Container makeStatPanel()
	{
		JPanel thisPanel = new JPanel();		
		thisPanel.setPreferredSize(new Dimension(200,200));
		
		JPanel subPanel = new JPanel();		
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));		
		numMove = new JLabel("Number of moves: ");			
		numTrial = new JLabel("Number of trials: ");
		avgMove = new JLabel("Avarage number of moves: ");		
		subPanel.add(numMove);
		subPanel.add(numTrial);
		subPanel.add(avgMove);
		thisPanel.add(subPanel);
	
		thisPanel.setBorder(BorderFactory.createTitledBorder("Statistic"));
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		return thisPanel;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		updateStat();
		repaint();
	}
	
	public void updateStat()
	{
		numMove.setText("Number of moves: " + agent.numMove);
		numTrial.setText("Number of trials: " + control.numTrial);
		avgMove.setText("Avarage number of moves: " + control.averageMove);
	}
	
	
	
	
}
