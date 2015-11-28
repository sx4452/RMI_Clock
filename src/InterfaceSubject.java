import java.rmi.*;
import java.util.*;


public interface InterfaceSubject extends Remote{
	public Date getTime()throws RemoteException;
	public void Attach(Observer observerObj,int interval)throws RemoteException;
	public void Detach(Observer observerObj)throws RemoteException;
}
