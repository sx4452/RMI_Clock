import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class InitialSubject {
	public static void main(String[] args){
		String registryURL;
		try{
			int RMIPortNum = 6666;
			startRegistry(RMIPortNum);
			Subject subjectImplObj = new Subject();
			registryURL = "rmi://localhost:"+RMIPortNum+"/callback";
			Naming.rebind(registryURL,subjectImplObj);
			try{
				InterfaceSubject subjectInfo = (InterfaceSubject)Naming.lookup(registryURL);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			System.out.println("Server Ready!");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	private static void startRegistry(int RMIPortNum)throws RemoteException{
		try{
			Registry registry = LocateRegistry.getRegistry(RMIPortNum);
			registry.list();
		}
		catch(RemoteException ex){
			Registry registry = LocateRegistry.createRegistry(RMIPortNum);
		}
	}
}
