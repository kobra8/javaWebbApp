package pl.javasurvival.HelloServer;


import io.vavr.collection.List;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class MessageBoardApplication {
	private List<Message> messages = List.empty();

	private MessageBoardApplication() {
		addMessage(new Message("Test content", "Zenek Tetstowy"));
		addMessage(new Message("Bla bla", "Sebastian Tester"));
	}

	public static void main(String[] args) {
		new MessageBoardApplication().serve();
	}
	private void serve() {
		RouterFunction route = nest( path("/api"),
				route(GET("/time"), renderTime())
						.andRoute(GET("/messages"), renderMessages())
						.andRoute(POST("/messages"), postMessage()));

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);

		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

		DisposableServer server = HttpServer.create().host("localhost").port(8082).handle(adapter).bindNow();

		System.out.println("press enter");

		Scanner sc = new Scanner(System.in);
		sc.next();
		server.disposeNow();
	}

	private HandlerFunction<ServerResponse> postMessage() {
		return request -> {
			Mono<Message> postedMessage = request.bodyToMono(Message.class);
			return postedMessage.flatMap(message -> {
				addMessage(message);
				return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(messages.toJavaList()));
			});
		};
	}

	private HandlerFunction<ServerResponse> renderMessages() {
		return request -> {
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(getMessages().toJavaList()));
		};
	}

	private HandlerFunction<ServerResponse> renderTime() {
		return request -> {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter myFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
			;
			return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue(myFormater.format(now)));
		};
	}

	private synchronized void addMessage(Message message) {
		messages = messages.append(message);
	}
	private  synchronized List<Message> getMessages() {
		return messages;
	}
}
