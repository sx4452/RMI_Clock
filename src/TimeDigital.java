import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.swing.*;

public class TimeDigital extends UnicastRemoteObject implements Observer 
	{
    private boolean temp;
	TimeDigital digitalTimeDisplay;
	DigitalTime time=new DigitalTime(new Date());
	TimeDigital()throws RemoteException{
		super();
		
	}

	public void setDigitalTimeDisplay(TimeDigital digitalTimeDisplay)
	{
		this.digitalTimeDisplay=digitalTimeDisplay;
		temp = true;
	}
	public void update(Date time)throws RemoteException
	{
		int nowh= time.getHours();
        int nowm= time.getMinutes();
        int nows= time.getSeconds();
		String timeStr;
        if(nowh<10) 
			timeStr="0"+nowh;
		else
			timeStr=""+nowh;
        if(nowm<10)  
			timeStr+=":0"+nowm;
		else
			timeStr+=":"+nowm;
        if(nows<10)
			timeStr+=":0"+nows;
		else 
			timeStr+=":"+nows;
		System.out.println("system current time:"+timeStr);
		this.time.displayTime(time);	
	}
	
	class DigitalTime extends JFrame
	{
		private JButton button1, button2;
		private JLabel label1,label2,label3,labelDig;
		private JTextField txt1, txt2;
		private JPanel panel1,panel2,panel3;
		InterfaceSubject subjectInf;
		
		public void displayTime(Date time){
			txt2.setText(time.toLocaleString());
		}
		
		public DigitalTime(Date date){
			super("time");
			button1=new JButton("connect");
			button2=new JButton("disconnect");
			button1.addActionListener(new connect());
			button2.addActionListener(new disConnect());
			
			
			label1=new JLabel("period");
			label2=new JLabel("seconds");
			label3=new JLabel("current time");
			labelDig=new JLabel("digital clock");
			
			txt1=new JTextField("1",2);
		    txt2=new JTextField(20);
		    panel1=new JPanel();
			panel2=new JPanel();
			panel3=new JPanel();
			panel1.setLayout(new FlowLayout());
			panel2.setLayout(new FlowLayout());
			
			panel1.add(labelDig);
			panel2.add(label1);
			panel2.add(txt1);
			panel2.add(label2);
			panel2.add(button1);
			panel2.add(button2);
			panel3.add(label3);
			panel3.add(txt2);
			
			Container con=getContentPane();
			con.add(BorderLayout.NORTH,panel1);
			con.add(BorderLayout.CENTER,panel3);                               
			con.add(BorderLayout.SOUTH,panel2);

            displayTime(date);
			
			setSize(400,200);
			setLocation(300,300);
			setVisible(true);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}
		
		class connect implements ActionListener
		{
			public void actionPerformed(ActionEvent e){
				try{
					if(temp)
					{
					String tick=txt1.getText();
					int timedue=Integer.parseInt(tick);
					String registryURL = "rmi://localhost"+":"+6666+"/callback";
				    subjectInf = (InterfaceSubject)Naming.lookup(registryURL);
					
					Date date=subjectInf.getTime();
					String time=date.toLocaleString();
					txt2.setText(time);
					subjectInf.Attach(digitalTimeDisplay, timedue);
					temp = false;
					}
					else
					{
                      messageBox();
					}
				}
				catch(Exception ex){
					JOptionPane.showMessageDialog(null,"sorry, server is not ready£¡", "notify", JOptionPane.ERROR_MESSAGE);
				}
			}
		   private void messageBox() {
		        JOptionPane.showMessageDialog(null,"sorry£¬digital time application is on£¬if wanna change period£¬please click 'disconnet'£¬then connect!", "notify", JOptionPane.ERROR_MESSAGE);
	        }
		}
		class disConnect implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
			    try
			    {
					if(!temp)
					{
			    	subjectInf.Detach(digitalTimeDisplay);
					temp = true;
					}
			    }
				catch(Exception ex)
				{
			    	ex.printStackTrace();
			    }
				
			}
		}	
    }
	public static void main(String []agrs)
		{
		try
		{
			TimeDigital digitalTimeDisplay=new TimeDigital();
			digitalTimeDisplay.setDigitalTimeDisplay(digitalTimeDisplay);
		}    
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}   
}
