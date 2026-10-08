package com.homeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.enums.*;
import com.homeservice.handler.websocket.*;
import com.homeservice.support.ProtocolTestBase;
import com.homeservice.utils.JwtTool;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.core.env.Environment;
import javax.crypto.SecretKey;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.Date;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
class WebSocketInfrastructureTest extends ProtocolTestBase {
    private final Environment environment;
    private final JwtTool jwt;
    private final SecretKey jwtSigningKey;
    private final ObjectMapper json;
    private final WebSocketSessionRegistry registry;
    private final AuthenticatedWebSocketHandler handler;
    private final HttpClient client=HttpClient.newHttpClient();
    private static class Listener implements WebSocket.Listener {
        final BlockingQueue<String> messages=new LinkedBlockingQueue<>();
        final CompletableFuture<Integer> close=new CompletableFuture<>();
        final StringBuilder text=new StringBuilder();
        public void onOpen(WebSocket ws) { ws.request(1); }
        public CompletionStage<?> onText(WebSocket ws,CharSequence data,boolean last) {
            text.append(data);if(last){messages.add(text.toString());text.setLength(0);}ws.request(1);return null;
        }
        public CompletionStage<?> onClose(WebSocket ws,int status,String reason) { close.complete(status);return null; }
        public void onError(WebSocket ws,Throwable error) { close.completeExceptionally(error); }
    }
    private URI uri(String suffix) { return URI.create("ws://localhost:"+environment.getProperty("local.server.port")+"/ws"+suffix); }
    private WebSocket connect(Listener listener) throws Exception { return client.newWebSocketBuilder().connectTimeout(Duration.ofSeconds(3)).buildAsync(uri(""),listener).get(5,TimeUnit.SECONDS); }
    private String frame(String token) throws Exception { return "{\"type\":\"AUTH\",\"accessToken\":"+json.writeValueAsString(token)+"}"; }
    @AfterEach void allConnectionsEventuallyCleaned() { await().atMost(Duration.ofSeconds(3)).untilAsserted(()->{assertThat(registry.size()).isZero();assertThat(handler.connectionCount()).isZero();}); }

    @Test void realConnectionAuthenticatesAndCleansAfterDisconnect() throws Exception {
        Listener listener=new Listener();WebSocket ws=connect(listener);
        assertThat(listener.messages.poll(100,TimeUnit.MILLISECONDS)).isNull();
        assertThat(registry.size()).isZero();
        ws.sendText(frame(jwt.createToken(2,Role.WORKER)),true).get(3,TimeUnit.SECONDS);
        String ack=listener.messages.poll(3,TimeUnit.SECONDS);
        assertThat(json.readTree(ack).path("type").asText()).isEqualTo("AUTHENTICATED");
        assertThat(json.readTree(ack).path("occurredAt").asText()).endsWith("+08:00");
        await().atMost(Duration.ofSeconds(2)).untilAsserted(()->assertThat(registry.size()).isEqualTo(1));
        ws.sendClose(WebSocket.NORMAL_CLOSURE,"done").get(3,TimeUnit.SECONDS);
        listener.close.get(3,TimeUnit.SECONDS);
    }
    @Test void missingAuthTimesOutWithinProtocolDeadline() throws Exception {
        Listener listener=new Listener();connect(listener);
        long started=System.nanoTime();
        assertThat(listener.close.get(7,TimeUnit.SECONDS)).isEqualTo(4401);
        assertThat(Duration.ofNanos(System.nanoTime()-started)).isBetween(Duration.ofSeconds(4),Duration.ofSeconds(7));
        assertThat(listener.messages).isEmpty();
    }
    @Test void invalidExpiredDisabledAndIllegalRoleCloseWithContractCodes() throws Exception {
        for(int scenario=0;scenario<5;scenario++) {
            String token=switch(scenario) {
                case 0 -> "invalid";
                case 1 -> Jwts.builder().claim("accountId","1").claim("role","CUSTOMER").expiration(Date.from(Instant.now().minusSeconds(10))).signWith(jwtSigningKey,Jwts.SIG.HS256).compact();
                case 2 -> jwt.createToken(4,Role.CUSTOMER);
                case 3 -> Jwts.builder().claim("accountId","1").claim("role","UNKNOWN").expiration(Date.from(Instant.now().plusSeconds(60))).signWith(jwtSigningKey,Jwts.SIG.HS256).compact();
                default -> jwt.createToken(5,Role.CUSTOMER);
            };
            Listener listener=new Listener();WebSocket ws=connect(listener);
            ws.sendText(frame(token),true).get(3,TimeUnit.SECONDS);
            assertThat(listener.close.get(3,TimeUnit.SECONDS)).isEqualTo(scenario<2?4401:4403);
            assertThat(listener.messages).isEmpty();
        }
    }
    @Test void tokenExpiryClosesAnAlreadyAuthenticatedConnection() throws Exception {
        String token=Jwts.builder().claim("accountId","1").claim("role","CUSTOMER").expiration(Date.from(Instant.now().plusSeconds(3))).signWith(jwtSigningKey,Jwts.SIG.HS256).compact();
        Listener listener=new Listener();WebSocket ws=connect(listener);
        ws.sendText(frame(token),true).get(3,TimeUnit.SECONDS);
        assertThat(listener.messages.poll(2,TimeUnit.SECONDS)).contains("AUTHENTICATED");
        assertThat(listener.close.get(4,TimeUnit.SECONDS)).isEqualTo(4401);
    }
    @Test void businessFramesAndUrlTokensAreRejected() throws Exception {
        Listener listener=new Listener();WebSocket ws=connect(listener);
        ws.sendText("{\"type\":\"CLAIM\",\"orderId\":\"1\"}",true).get(3,TimeUnit.SECONDS);
        assertThat(listener.close.get(3,TimeUnit.SECONDS)).isEqualTo(4401);
        assertThat(listener.messages).isEmpty();
        Listener authenticated=new Listener();WebSocket second=connect(authenticated);
        second.sendText(frame(jwt.createToken(1,Role.CUSTOMER)),true).get(3,TimeUnit.SECONDS);
        assertThat(authenticated.messages.poll(2,TimeUnit.SECONDS)).contains("AUTHENTICATED");
        second.sendText("{\"type\":\"CLAIM\"}",true).get(3,TimeUnit.SECONDS);
        assertThat(authenticated.close.get(3,TimeUnit.SECONDS)).isEqualTo(4403);
        assertThatThrownBy(()->client.newWebSocketBuilder().buildAsync(uri("?token=forbidden"),new Listener()).get(3,TimeUnit.SECONDS)).hasCauseInstanceOf(WebSocketHandshakeException.class);
        assertThatThrownBy(()->client.newWebSocketBuilder().header("Origin","https://untrusted.invalid").buildAsync(uri(""),new Listener()).get(3,TimeUnit.SECONDS)).hasCauseInstanceOf(WebSocketHandshakeException.class);
    }
}
