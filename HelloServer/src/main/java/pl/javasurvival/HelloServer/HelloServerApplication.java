package pl.javasurvival.HelloServer;


import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {
	private AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		new HelloServerApplication().serve();
	}
	private void serve() {
		RouterFunction route = route(GET("/"),
				renderWelcome());

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);

		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

		DisposableServer server = HttpServer.create().host("localhost").port(8082).handle(adapter).bindNow();

		System.out.println("press enter");

		Scanner sc = new Scanner(System.in);
		sc.next();
		server.disposeNow();
	}

	private HandlerFunction<ServerResponse> renderWelcome() {
		return request -> {
			Optional<String> userName = request.queryParam("userName");
			String welcomeHtml = String.format("<h1>Witaj %s na stronie obozu przetrwrania</h1>",
								userName.orElse("Nieznany"));
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter myFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
			String time = "<p>Czas to: " + now.format(myFormater) + "</p>";
			String visits = "<p>To są " + counter + " odwiedziny.</p>";
			String inputHtml = "<input type='text' name='userName'>";
			String submithtml = "<input type='submit' value='Wyślij'>";
			String formHtml = String.format("<form>%s %s</form>", inputHtml, submithtml);
			counter.incrementAndGet();
			return ServerResponse.ok().contentType(new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"))).body(BodyInserters.fromValue("<body>" +
					welcomeHtml + time + visits + formHtml +
					"</body>"));
		};
	}

}
