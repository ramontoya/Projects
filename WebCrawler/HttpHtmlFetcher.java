import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * A class designed to make fetching the results of different HTTP operations
 * easier. This particular class handles the GET operation.
 * 
 * @author Sophie Engle
 * @author CS 212 Software Development
 * @author University of San Francisco
 * 
 * @see HttpHtmlFetcher
 * @see HeaderFetcher
 */
public class HttpHtmlFetcher {
	/** Port used by socket. For web servers, should be port 80. */
	private static final int PORT = 80;

	/** The URL to fetch from a web server. */
	private final URL url;

	/** Used to determine if headers have been read. */
	private boolean head;

	/**
	 * Initializes this fetcher. Must call {@link #fetch()} to actually start
	 * the process.
	 * 
	 * @param url
	 *            - the link to fetch from the webserver
	 * @throws MalformedURLException
	 *             if unable to parse URL
	 */
	public HttpHtmlFetcher(String url) throws MalformedURLException {
		this.url = new URL(url);
		head = true;
	}

	/**
	 * Returns the port being used to fetch URLs.
	 * 
	 * @return port number
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * Returns the URL being used by this fetcher.
	 * 
	 * @return URL
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * Crafts the HTTP request from the URL. Must be overridden.
	 * 
	 * @return HTTP request
	 */
	public String craftRequest() {
		String host = this.getURL().getHost();
		String resource = this.getURL().getFile().isEmpty() ? "/" : this
				.getURL().getFile();

		StringBuffer output = new StringBuffer();
		output.append("GET " + resource + " HTTP/1.1\n");
		output.append("Host: " + host + "\n");
		output.append("Connection: close\n");
		output.append("\r\n");

		return output.toString();
	}

	/**
	 * Will skip any headers returned by the web server, and then output each
	 * line of HTML to the console.
	 * 
	 * @param line
	 *            - text retrieved from web server
	 * @return htmlLines - StringBuffer storing the processed line
	 * 
	 */
	public StringBuffer processLine(String line) {
		StringBuffer htmlLines = new StringBuffer();

		if (head) {
			// Check if we hit the blank line separating headers and HTML
			if (line.trim().isEmpty()) {
				head = false;
			}
		} else {
			htmlLines.append(line + " "); // store all the returned HTML
		}
		return htmlLines;
	}

	/**
	 * Will connect to the web server and fetch the URL using the HTTP request
	 * from {@link #craftRequest()}, and then call {@link #processLine(String)}
	 * on each of the returned lines.
	 */
	public String fetch() {
		StringBuffer lines = new StringBuffer();
//		System.out.println("Server: " + url.getHost() + ":" + PORT);

		try (Socket socket = new Socket(url.getHost(), PORT);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream());) {
			String request = craftRequest();
//			System.out.println("HTTP: " + request);

			writer.println(request);
			writer.flush();

			String line = reader.readLine();

			while (line != null) {

				lines.append(processLine(line));

				line = reader.readLine();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lines.toString();
	}
}