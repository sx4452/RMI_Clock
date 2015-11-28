import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.swing.*;

public class TimeAnalog extends UnicastRemoteObject implements Observer {
	TimeAnalog analogTimeDisplay ;
	AnalogTime time=new AnalogTime(new Date());
	TimeAnalog()throws RemoteException{
		super();
	}

	public void setAnalogTimeDisplay (TimeAnalog analogTimeDisplay){
		this.analogTimeDisplay =analogTimeDisplay ;
	}
	public void update(Date time)throws RemoteException{
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
		System.out.println("System Current Time:"+timeStr);
		
		this.time.clock.set(time);
	    this.time.repaint();	
	}
	
	class AnalogTime extends JFrame{
		private JButton button1, button2;
		private JLabel label1,label2,labelAna;
		private JTextField txt1;
		private JPanel panel1,panel2;
		InterfaceSubject subjectInf;
		
		Clock clock;		
		public AnalogTime(Date date){
			super("time");
			button1=new JButton("connect");
			button2=new JButton("disconnect");
			button1.addActionListener(new connect());
			button2.addActionListener(new disConnect());
			
			label1=new JLabel("period");
			label2=new JLabel("seconds");
			labelAna=new JLabel("analogtime");
			
			txt1=new JTextField("1",2);
		    panel1=new JPanel();
			panel2=new JPanel();
		
			panel1.setLayout(new FlowLayout());
			panel2.setLayout(new FlowLayout());
			
			panel1.add(labelAna);
			panel2.add(label1);
			panel2.add(txt1);
			panel2.add(label2);
			panel2.add(button1);
			panel2.add(button2);
					
			clock=new Clock(date);
			clock.setLayout(new FlowLayout());
			Container con=getContentPane();
			con.add(BorderLayout.NORTH,panel1);
			con.add(BorderLayout.CENTER,clock);                                
			con.add(BorderLayout.SOUTH,panel2);

			setSize(400,300);
			setLocation(300,300);
			setVisible(true);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}
		
		class Clock extends JPanel{

		    int x,y,x0,y0,r,h,olds_x,olds_y,oldm_x,oldm_y,oldh_x,oldh_y,ss,mm,hh,old_m,old_h,ang;
		    final double RAD=Math.PI/180;
		    Date date;
		    public Clock(Date date){
		    	this.date=date;
		        setSize(1080,760);
		    }

		    public void set(Date date){
		    	this.date=date;
		    }
		    public void reDarw(){
				repaint();
			}
		    public void paint(Graphics g){
		        Graphics2D g2D = (Graphics2D)g;
		        Insets insets = getInsets();
		        int L = insets.left/2,T = insets.top/2;
		        h = getSize().height;
		        g2D.setStroke(new BasicStroke(4.0f));
		        g.drawOval(L+140,T+40,h-80,h-80);
		        r=h/2-40;
		        x0=40+r-5+L;
		        y0=40+r-5-T;
		        ang=60;
		        for(int i=1;i<=12;i++){
		            x=(int)((r+10)*Math.cos(RAD*ang)+x0);
		            y=(int)((r+10)*Math.sin(RAD*ang)+y0);
		            g.drawString(""+i,x+100,h-y);
		            ang-=30;
		        }
		        int nowh= date.getHours();
		        int nowm= date.getMinutes();
		        int nows= date.getSeconds();
		   
		        ss=90-nows*6;
		        mm=90-nowm*6;
		        hh=90-nowh*30-nowm/2;
		        x0=r+140+L;
		        y0=r+40+T;
		        g2D.setStroke(new BasicStroke(1.2f));
		        if(olds_x>0){
		            g.setColor(getBackground());
		            g.drawLine(x0,y0,olds_x,h-olds_y);
		        }
		        else{
		            old_m = mm;
		            old_h = hh;
		        }
		        x=(int)(r*0.9*Math.cos(RAD*ss))+x0;
		        y=(int)(r*0.9*Math.sin(RAD*ss))+y0-2*T;
		        g.setColor(Color.green);
		        g.drawLine(x0,y0,x,h-y);
		        olds_x=x;
		        olds_y=y;
		        g2D.setStroke(new BasicStroke(2.2f));
		        if(old_m!=mm){
		            g.setColor(getBackground());
		            g.drawLine(x0,y0,oldm_x,h-oldm_y);
		        }
		        x=(int)(r*0.7*Math.cos(RAD*mm))+x0;
		        y=(int)(r*0.7*Math.sin(RAD*mm))+y0-2*T;
		        g.setColor(Color.red);
		        g.drawLine(x0,y0,x,h-y);
		        oldm_x=x;
		        oldm_y=y;
		        old_m=mm;
		        g2D.setStroke(new BasicStroke(3.4f));
		        if(old_h!=hh){
		            g.setColor(getBackground());
		            g.drawLine(x0,y0,oldh_x,h-oldh_y);
		        }
		        x=(int)(r*0.5*Math.cos(RAD*hh))+x0;
		        y=(int)(r*0.5*Math.sin(RAD*hh))+y0-2*T;
		        g.setColor(Color.blue);
		        g.drawLine(x0,y0,x,h-y);
		        oldh_x=x;
		        oldh_y=y;
		        old_h=hh;        
		    }
		}
		
		class connect implements ActionListener{
			public void actionPerformed(ActionEvent e){
				try{
					String tick=txt1.getText();
					int timedue=Integer.parseInt(tick);
					String registryURL = "rmi://localhost"+":"+6666+"/callback";
				    subjectInf = (InterfaceSubject)Naming.lookup(registryURL);
					subjectInf.Attach(analogTimeDisplay, timedue);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		class disConnect implements ActionListener{
			public void actionPerformed(ActionEvent e){
			    try{
			    	subjectInf.Detach(analogTimeDisplay);
			    }catch(Exception ex){
			    	ex.printStackTrace();
			    }
			}
		}	
    }
	public static void main(String []agrs){
		try{
			TimeAnalog analogTimeDisplay =new TimeAnalog();
			analogTimeDisplay.setAnalogTimeDisplay (analogTimeDisplay);
		}    
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
