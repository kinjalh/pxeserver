package org.kj.pxe.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;

/**
 * Reads the specifed properties file and if successful, launches 2 concurrently
 * running servers - One {@link DiscoverServer} and one {@link RequestServer}
 * 
 * @author Kinjal
 *
 */
class ServerImpl {

	/**
	 * Runs using the properties file name specified at runtime as args[0]
	 */
	void run() {
		DhcpSettings settings = null;
		try {
			settings = new DhcpSettingsImpl("/dhcp.properties");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		ExecutorService pool = Executors.newFixedThreadPool(2);
		pool.execute(new DiscoverServer(settings));
		pool.execute(new RequestServer(settings));
	}

	/**
	 * runs the program. args[0] MUST be the name of the properties file
	 * 
	 * @param args
	 *            args[0] must be the properties file name
	 */
	public static void main(String[] args) {
		(new ServerImpl()).run();
	}
}
