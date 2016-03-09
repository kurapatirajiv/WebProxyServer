package webproxyserver;

/**
 *
 * @author Rajiv K
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {

    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
    private static ServerSocket socket;

    /** Create the ProxyCache object and the socket */
    public static void init(int p) {
        port = p;
        try {
            // Creating socket with the port number that is being sent from the main method
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating socket: " + e);
            System.exit(-1);
        }
    }

    public static void handle(Socket client) {
        Socket server = null;
        HttpRequest request = null;
        HttpResponse response = null;

        /* Process request. If there are any exceptions, then simply
         * return and end this request. This unfortunately means the
         * client will hang for a while, until it timeouts. */
        /* Read request by using BufferedReader*/

        try {
            /* Fill in */
            BufferedReader ClientBrowserReq = new BufferedReader(new InputStreamReader(client.getInputStream()));
            request = new HttpRequest(ClientBrowserReq);
        } catch (IOException e) {
            System.out.println("Error reading request from client: " + e);
            return;
        }
        /* Send request to server by using DataOutputStream and writeBytes */
        try {
            /* Open socket and write request to socket */
            /* Fill in */
            server = new Socket(request.getHost(), request.getPort());
            DataOutputStream FwdReqtoServer = new DataOutputStream(server.getOutputStream());
            FwdReqtoServer.writeBytes(request.toString());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + request.getHost());
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println("Error writing request to server: " + e);
            return;
        }
        /* Read response and forward it to client by using DataInputStream and DataOutputStream */
        try {
            /* Fill in */
            /* Write response to client. First headers by using writeBytes, then body by using write */
            byte[] cache = CachePage.isCached(request.URI);
            if (cache.length == 0) {
                DataInputStream fromServer = new DataInputStream(server.getInputStream());
                //Create a response object
                response = new HttpResponse(fromServer);
                DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
                //Write the reposne headers first in string format
                toClient.writeBytes(response.toString());
                //Write the body of the response
                toClient.write(response.body);
                //Since the page doesnot exist in our local cache,Cache the page
                CachePage.cacheIt(request, response);

            } else {
                //Page found in local cache so write the response from the local cache
                DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
                toClient.write(cache);
            }


            client.close();
            server.close();

        } catch (IOException e) {
            System.out.println("Error writing response to client: " + e);
        }

    }

    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
        int myPort = 0;

        try {
            System.out.println("Please enter the port number");
            Scanner stdin = new Scanner(System.in);
            myPort = stdin.nextInt();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need port number as argument");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid Port Number.");
            System.exit(-1);
        }
        //Create a serversocket with the given port
        System.out.println("Socket Connection Initiated at the port: " + myPort);
        init(myPort);

        /** Main loop. Listen for incoming connections and spawn a new
         * thread for handling them */
        Socket client = null;

        while (true) {
            try {
                //Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                client = socket.accept();
                // A new thread is created for every client request that made through the browser
                Thread t = new Thread(new RunThread(client));
                t.start();
                // After the thread is kicked off we handle the client request                
            } catch (IOException e) {
                System.out.println("Error reading request from client: " + e);
                /* Definitely cannot continue processing this request,
                 * so skip to next iteration of while loop. */
                continue;
            }
        }
    }
}
