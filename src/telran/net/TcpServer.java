package telran.net;

import java.net.*;
import java.util.concurrent.*;

public class TcpServer extends Server {
	private static final int TIMEOUT = 5_000;
	private ServerSocket serverSocket;
	private static ExecutorService executor;
	int nThreads = Runtime.getRuntime().availableProcessors();
	private static boolean flShutdown = false;
	private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

	public static boolean isFlShutdown() {
		return flShutdown; 
	}

	public static int getQueue() {
		return ((ThreadPoolExecutor)executor).getQueue().size();
	}
	public TcpServer(Protocol protocol, int port) throws Exception {
		super(protocol, port);
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(TIMEOUT);
		System.out.println("Number of threads in Threads Pool is :" + nThreads);
		executor = new ThreadPoolExecutor( 0, 2, TIMEOUT, TimeUnit.SECONDS, queue);  // TODO: set back nThreads instead of the 2nd parameter
	}

	public void shutdown() {
		flShutdown = true;
		executor.shutdownNow();
	}

	@Override
	public void run() {
		System.out.println("Tcp server listening on port " + this.port);
		while (true && !flShutdown) {
			try {
				Socket socket = serverSocket.accept();
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				executor.execute(serverClient);
			} catch (SocketTimeoutException s) {
				if (flShutdown) { 
					System.out.println("Server's been shutdown");
					try {
						serverSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				break;
			}
		}
	}

}
