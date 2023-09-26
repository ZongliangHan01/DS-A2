package server;

import src.main.java.remote.IRemoteGame;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Creates an instance of the RemoteMath class and
 * publishes it in the rmiregistry
 *
 */
public class RMIServer {

    public static void main(String[] args)  {

        try {

            //Export the remote math object to the Java RMI runtime so that it
            //can receive incoming remote calls.
            //Because RemoteMath extends UnicastRemoteObject, this
            //is done automatically when the object is initialized.
            //
            //RemoteMath obj = new RemoteMath();
            //IRemoteMath stub = (IRemoteMath) UnicastRemoteObject.exportObject(obj, 0);
            //

            IRemoteGame remoteGame = new RemoteGame();

            //Publish the remote object's stub in the registry under the name "Compute"
//            Registry registry = LocateRegistry.getRegistry();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("TicTacToe", remoteGame);

            System.out.println("TicTacToe Server ready");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ((RemoteGame) remoteGame).checkAlive();
                }

            });
            thread.start();


            ((RemoteGame) remoteGame).cleanUp();
            //The server will continue running as long as there are remote objects exported into
            //the RMI runtime, to re	move remote objects from the
            //RMI runtime so that they can no longer accept RMI calls you can use:
            // UnicastRemoteObject.unexportObject(remoteMath, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
