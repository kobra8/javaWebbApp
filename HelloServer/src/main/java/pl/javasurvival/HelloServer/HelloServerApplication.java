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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {

	public static void main(String[] args) {
		new HelloServerApplication().serve();
	}
	private void serve() {
		RouterFunction route = route(GET("/api/time"),
				getTime());

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);

		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

		DisposableServer server = HttpServer.create().host("localhost").port(8082).handle(adapter).bindNow();

		System.out.println("press enter");

		Scanner sc = new Scanner(System.in);
		sc.next();
		server.disposeNow();
	}

	private HandlerFunction<ServerResponse> getTime() {
		return request -> {

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter myFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
			;
			return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue(myFormater.format(now)));
		};
	}

}
