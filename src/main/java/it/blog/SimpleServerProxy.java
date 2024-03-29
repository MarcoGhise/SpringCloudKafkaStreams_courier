package it.blog;

//This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)
//Copyright (c) 1997 by David Flanagan
//This example is provided WITHOUT ANY WARRANTY either expressed or implied.
//You may study, use, modify, and distribute it for non-commercial purposes.
//For any commercial use, see http://www.davidflanagan.com/javaexamples

import java.io.*;
import java.net.*;
import java.security.Timestamp;
import java.util.Date;

/**
 * This class implements a simple single-threaded proxy server.
 **/
public class SimpleServerProxy {
	/** The main method parses arguments and passes them to runServer */
	public static void main(String[] args) throws IOException {
		try {
			// Check the number of arguments
			if (args.length != 3)
				throw new IllegalArgumentException("Wrong number of arguments.");

			// Get the command-line arguments: the host and port we are proxy for
			// and the local port that we listen for connections on
			String host = args[0];
			int remoteport = Integer.parseInt(args[1]);
			int localport = Integer.parseInt(args[2]);
			// Print a start-up message
			System.out.println("Starting proxy for " + host + ":" + remoteport + " on port " + localport);
			// And start running the server
			runServer(host, remoteport, localport); // never returns
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Usage: java SimpleProxyServer " + "<host> <remoteport> <localport>");
		}
	}

	/**
	 * This method runs a single-threaded proxy server for host:remoteport on the
	 * specified local port. It never returns.
	 * @throws InterruptedException 
	 **/
	public static void runServer(String host, int remoteport, int localport) throws IOException, InterruptedException {
		// Create a ServerSocket to listen for connections with
		ServerSocket ss = new ServerSocket(localport);

		// Create buffers for client-to-server and server-to-client communication.
		// We make one final so it can be used in an anonymous class below.
		// Note the assumptions about the volume of traffic in each direction...
		final byte[] request = new byte[1024];
		byte[] reply = new byte[4096];

		// This is a server that never returns, so enter an infinite loop.
		while (true) {
			// Variables to hold the sockets to the client and to the server.
			Socket client = null, server = null;
			try {
				// Wait for a connection on the local port
				client = ss.accept();

				// Get client streams. Make them final so they can
				// be used in the anonymous thread below.
				final InputStream from_client = client.getInputStream();
				final OutputStream to_client = client.getOutputStream();

				System.out.println((new Date()).getTime());
				
				System.out.println(client.hashCode());
				
				Thread.sleep(5000);
			
				// The server closed its connection to us, so close our
				// connection to our client. This will make the other thread exit.
				to_client.close();
			} catch (IOException e) {
				System.err.println(e);
			}
			// Close the sockets no matter what happens each time through the loop.
			finally {
				try {
					if (server != null)
						server.close();
					if (client != null)
						client.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}
}
