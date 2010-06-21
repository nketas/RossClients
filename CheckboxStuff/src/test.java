import java.awt.*;

import java.awt.event.*;

import java.applet.*;
public class test extends Applet implements ActionListener  {

	
	private Choice RossDevices;
	private CheckboxGroup InfoCommunicationOptions;
	private Checkbox SendInfo, ReceiveInfo;
	private Panel DeviceChoicePanel, InfoCommunicationOptionsPanel;
	
	
	public void init()
	{
		setLayout(new GridLayout(3,0));

		RossDevices = new Choice();
		RossDevices.add("device 1");
		RossDevices.add("device 2");
		DeviceChoicePanel = new Panel();
		DeviceChoicePanel.add(RossDevices);
		add(DeviceChoicePanel);
	}
	
	
	public test()
	{
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String DeviceChoice = arg0.getActionCommand();
		if(DeviceChoice != null)
		System.out.println(DeviceChoice);
		
	}
	
	
	
}
