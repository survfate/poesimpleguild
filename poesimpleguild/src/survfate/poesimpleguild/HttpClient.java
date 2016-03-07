package survfate.poesimpleguild;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import survfate.poesimpleguild.ui.MainPanel;
import survfate.poesimpleguild.utils.InvalidGuildIDException;

public class HttpClient {
	public static OkHttpClient httpClient;
	public static int maxRetries = 5;

	public static void createHttpClient() {
		httpClient = new OkHttpClient();
		setMaxRetries(maxRetries);
	}

	public static void setMaxRetries(int maxRetries) {
		httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
		httpClient.setReadTimeout(15, TimeUnit.SECONDS);
		httpClient.interceptors().add(new LoggingInterceptor());
		httpClient.interceptors().add(new RetryInterceptor());
		// httpClient.networkInterceptors()
	}

	// Return the OkHttpClient responses

	public static String runURL(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code: " + response);
		}
		String result = response.body().string();
		response.body().close();
		return result;
	}

	public static String runURLWithRequestBody(String url, RequestBody requestBody) throws IOException {
		Request request = new Request.Builder().url(url).post(requestBody).build();
		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code: " + response);
		}
		String result = response.body().string();
		response.body().close();
		return result;
	}

	public static String runGuildURL(String url) throws IOException, InvalidGuildIDException {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			if (url.contains("http://www.pathofexile.com/guild/profile/") && (response.code() == 404))
				throw new InvalidGuildIDException("Unexpected code: " + response);
			else
				throw new IOException("Unexpected code: " + response);
		}
		String result = response.body().string();
		response.body().close();
		return result;
	}
}

class LoggingInterceptor implements Interceptor {
	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();
		System.out.println(
				String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		System.out.println(String.format("Received response for %s in %.1fms%n%s", response.request().url(),
				(t2 - t1) / 1e6d, response.headers()));

		return response;
	}
}

class RetryInterceptor implements Interceptor {
	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {

		Request request = chain.request();
		Response response = null;

		// Try the request
		try {
			response = chain.proceed(request);
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
		}

		boolean isUnsuccessful = false;
		try {
			isUnsuccessful = !response.isSuccessful();
		} catch (java.lang.NullPointerException e) {
			isUnsuccessful = true;
			// e.printStackTrace();
		}

		int count = 1;
		while (isUnsuccessful && count <= HttpClient.maxRetries) {
			MainPanel.logOutput
					.append("Error: Connection failed! Retries: " + count + "/" + HttpClient.maxRetries + "\n");
			if (count == HttpClient.maxRetries) {
				int reply = JOptionPane
						.showConfirmDialog(null,
								"Connection timed out after " + HttpClient.maxRetries
										+ " try. Do you want to retry for " + HttpClient.maxRetries + " more time?",
								"Error", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
					count = 0;
				else
					JOptionPane.showMessageDialog(null, "Connection timed out! Please try again.", "Error", 0);
			}

			// Retry the request
			try {
				response = chain.proceed(request);
			} catch (java.net.SocketTimeoutException e) {
				// e.printStackTrace();
			}
			count++;
		}

		// Otherwise just pass the original response on
		return response;
	}
}
