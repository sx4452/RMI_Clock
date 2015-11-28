import java.rmi.*;
import java.util.*;

public interface Observer extends Remote {
	 public void update(Date time)throws RemoteException;
}
