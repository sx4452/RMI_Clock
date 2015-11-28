import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class Subject extends UnicastRemoteObject implements InterfaceSubject {
	Timer time;
	private Vector observerList;  
	private Vector intervalList;
	private Vector intervalTempList;
	
    public Subject()throws RemoteException{
    	super();
    	observerList=new Vector();
    	intervalList=new Vector();
    	intervalTempList=new Vector();
    	time=new Timer();
        Notify notify=new Notify();
    	time.schedule(notify,0,1000);  	
    }
    
    public Date getTime()throws RemoteException{
    	Date date=new Date();
    	return date;
    }
    
    public void Attach(Observer observerObj,int interval)throws RemoteException{  
    	observerList.addElement(observerObj);
    	Integer inter=new Integer(interval);
    	intervalList.addElement(inter);
    	intervalTempList.addElement(inter);
    }
    
	public void Detach(Observer observerObj)throws RemoteException{    
		int index=observerList.indexOf(observerObj);
		observerList.remove(index);
		intervalList.remove(index);
		intervalTempList.remove(index);
	}

	
	class Notify extends TimerTask{
		public void run(){
			for(int i=0;i<observerList.size();i++){
				Observer observerObj=(Observer)observerList.elementAt(i);
				int count=((Integer)intervalTempList.elementAt(i)).intValue();
				count--;
				if(count==0){
					count=((Integer)intervalList.elementAt(i)).intValue();
					try{
						Date date=getTime();
					    observerObj.update(date);
					}catch(RemoteException ex)
					{
						ex.printStackTrace();
					}
					
				}
				Integer ocount=new Integer(count);
				intervalTempList.set(i, ocount);
				}
			}
			
		}
}

