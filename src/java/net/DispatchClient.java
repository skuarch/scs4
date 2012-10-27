package net;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;
import org.apache.log4j.Logger;
import util.IOUtilities;

/**
 * dispatch each client
 *
 * @author skuarch
 */
public class DispatchClient {

    private static final Logger logger = Logger.getLogger(DispatchClient.class);
    private InputStream myInputStream = null;
    private OutputStream myOutputStream = null;
    private ObjectOutputStream myObjectOutputStream = null;
    private ObjectInputStream myObjectInputStream = null;
    private OutputStream remoteOutputStream = null;
    private ObjectOutputStream remoteObjectOutputStream = null;
    private InputStream remoteInputStream = null;
    private ObjectInputStream remoObjectInputStream = null;
    private Socket remoteSocket = null;
    private String remoteServer = null;
    private int remotePort = 8081;

    //==========================================================================
    /**
     * create a instance.
     *
     * @param myInputStream InputStream
     * @param myOutputStream OutputStream
     */
    public DispatchClient(InputStream myInputStream, OutputStream myOutputStream) {
        this.myInputStream = myInputStream;
        this.myOutputStream = myOutputStream;
        this.remoteServer = "192.168.208.32";
    } // end DispatchClient

    //==========================================================================
    /**
     * in this method the main server receives the object and sends the object
     * to remote server
     */
    public void transferData() {

        if (myInputStream == null) {
            throw new NullPointerException("myInputStream is null");
        }

        if (myOutputStream == null) {
            throw new NullPointerException("myOutputStream is null");
        }

        Object objectClient = null;
        Object objectRemote = null;

        try {

            logger.info("attending client ");

            // wainting for a object
            objectClient = receiveObjectFromClient();

            //if doesn't exist reponse, do somenthing
            if (objectClient == null) {
                //do something
                return;
            }

            //tranfer object to remote server
            remoteSocket = new Socket(remoteServer, remotePort);
            transferObjectRemoteServer(objectClient);

            //wainting response from remote server
            objectRemote = receiveObjectFromRemoteServer();

            //resend remoteObject to client
            sendObjectClient(objectRemote);

        } catch(NoRouteToHostException nrthe){    
            sendErrorCLient(new Exception(nrthe.getMessage() + " " + remoteServer + " " + remotePort));
        } catch (Exception e) {
            sendErrorCLient(e);
            logger.error(e);
            e.printStackTrace();
        } finally {
            closer();
        }

    } // end transferData

    //==========================================================================
    /**
     * return to client the new object.
     *
     * @param objectRemote Object
     */
    public void sendObjectClient(Object objectRemote) {

        try {
            myObjectOutputStream = new ObjectOutputStream(myOutputStream);
            myObjectOutputStream.writeObject(objectRemote);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

    } // end sendObjectClient

    //==========================================================================
    /**
     * when a client is connected we are waiting for something<br/> this method
     * can return null
     *
     * @return Object
     */
    private Object receiveObjectFromClient() {

        Object object = null;

        try {

            myObjectInputStream = new ObjectInputStream(myInputStream);

            while (true) {
                object = myObjectInputStream.readObject();
                if (object != null) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        return object;

    } // end receiveObject

    //==========================================================================
    /**
     * transfer object to remote server.
     *
     * @param remoteServer String IP address or hostname.
     * @param port int port of remote server.
     * @param object object
     */
    private void transferObjectRemoteServer(Object object) {

        try {

            remoteOutputStream = remoteSocket.getOutputStream();
            remoteObjectOutputStream = new ObjectOutputStream(remoteOutputStream);
            remoteObjectOutputStream.writeObject(object);
            remoteObjectOutputStream.flush();

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

    } // end transferObject

    //==========================================================================
    private Object receiveObjectFromRemoteServer() {

        Object object = null;

        try {

            remoteInputStream = remoteSocket.getInputStream();
            remoObjectInputStream = new ObjectInputStream(remoteInputStream);

            while (true) {
                object = remoObjectInputStream.readObject();
                if (object != null) {
                    break;
                }
            }

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        return object;

    } // end receiveObjectFromRemoteServer

    //==========================================================================
    private void closer() {
        IOUtilities.closeOutputStream(remoteObjectOutputStream);
        IOUtilities.closeOutputStream(remoteOutputStream);
        IOUtilities.closeInputStream(remoteInputStream);
        IOUtilities.closeInputStream(remoObjectInputStream);
        IOUtilities.closeInputStream(myInputStream);
        IOUtilities.closeInputStream(myObjectInputStream);
        IOUtilities.closeOutputStream(myOutputStream);
        IOUtilities.closeOutputStream(myObjectOutputStream);
        IOUtilities.closeSocket(remoteSocket);
    } // end closer

    //==========================================================================
    @Override
    protected void finalize() throws Throwable {

        logger.info("cleaning DispatchClient");
        
        try {
            closer();
            myInputStream = null;
            myOutputStream = null;
            myObjectOutputStream = null;
            myObjectInputStream = null;
            remoteOutputStream = null;
            remoteObjectOutputStream = null;
            remoteInputStream = null;
            remoObjectInputStream = null;
            remoteSocket = null;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            super.finalize();
        }
    }
    // end finalize
    
    //==========================================================================
    private void sendErrorCLient(Object object){
    
        try {
            myObjectOutputStream = new ObjectOutputStream(myOutputStream);
            myObjectOutputStream.writeObject(object);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        
    } // end sendErrorCLient
    
} // end class
