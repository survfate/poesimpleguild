package survfate.poesimpleguild;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import survfate.poesimpleguild.utils.InvalidGuildIDException;

public class HttpClient {
	public static OkHttpClient httpClient;

	public static void createHttpClient() {
		httpClient = new OkHttpClient();
		httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
		httpClient.setReadTimeout(15, TimeUnit.SECONDS);
		// httpClient.interceptors().add(new LoggingInterceptor());
	}

	/* Return the OkHttpClient responses */

	public static boolean testURL(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			return false;
		}
		response.body().close();
		return true;
	}

	public static String runURL(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
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
			response.body().close();
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
			response.body().close();
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
