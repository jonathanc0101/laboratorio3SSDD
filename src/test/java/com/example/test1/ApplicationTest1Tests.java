package com.example.test1;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled

class ApplicationTest1Tests {

	String getSaldo = "/saldo";
	String postDeposito = "/deposito";

	String url1 = "http://localhost:8081/api";
	String url2 = "http://localhost:8082/api";
	String url3 = "http://localhost:8083/api";


	@Test
	public void simuladorTest() throws Exception {

		double saldoFinalEsperado1 = 555;
		double saldoFinalEsperado2 = 1986;
		double saldoFinalEsperado3 = 2047;


		WebClient webClient1 = WebClient.builder()
				.baseUrl(url1)
				.build();

		WebClient webClient2 = WebClient.builder()
				.baseUrl(url2)
				.build();

		WebClient webClient3 = WebClient.builder()
				.baseUrl(url3)
				.build();

		Saldo saldoInicial1 = getSaldoActual(webClient1);
		Saldo saldoInicial2 = getSaldoActual(webClient2);
		Saldo saldoInicial3 = getSaldoActual(webClient3);

		saldoFinalEsperado1 += saldoInicial1.saldo;
		saldoFinalEsperado2 += saldoInicial2.saldo;
		saldoFinalEsperado3 += saldoInicial3.saldo;

		hacerPostDeposito(100,webClient1);
		hacerPostDeposito(100,webClient1);
		hacerPostDeposito(100,webClient1);
		hacerPostDeposito(100,webClient1);
		hacerPostDeposito(100,webClient1);
		hacerPostDeposito(55,webClient1);

		hacerPostDeposito(1000,webClient2);
		hacerPostDeposito(900,webClient2);
		hacerPostDeposito(80,webClient2);
		hacerPostDeposito(6,webClient2);

		hacerPostDeposito(1000,webClient3);
		hacerPostDeposito(1000,webClient3);
		hacerPostDeposito(47,webClient3);


		Saldo saldoFinal1 = getSaldoActual(webClient1);
		Saldo saldoFinal2 = getSaldoActual(webClient2);
		Saldo saldoFinal3 = getSaldoActual(webClient3);

		assertEquals(saldoFinalEsperado1,saldoFinal1.saldo );
		assertEquals(saldoFinalEsperado2,saldoFinal2.saldo);
		assertEquals(saldoFinalEsperado3,saldoFinal3.saldo);


		System.out.println("TEST PASSED SUCCESSFULLY");

	}

	Saldo getSaldoActual(WebClient webClient){
		return webClient.get()
				.uri(getSaldo)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Saldo.class)
				.block();
	}

	void hacerPostDeposito(double valor, WebClient webClient){
		TransaccionModel monto = new TransaccionModel(valor);

		webClient.post()
				.uri(postDeposito)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON )
				.body(Mono.just(monto), TransaccionModel.class)
				.retrieve()
				.bodyToMono(TransaccionModel.class)
				.block();
	}

}
