package com.example.test1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTest1Tests {

	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private TransaccionRepository transaccionRepository;

	@BeforeEach
	void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	@Order(1)
	public void simuladorTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

		transaccionRepository.deleteAll();
		List<TransaccionModel> transacciones = transaccionRepository.findAll();
		assertEquals(0, transacciones.size());

		//transaccion auxiliar
		TransaccionModel transaccionAux = new TransaccionModel();
		transaccionAux.monto = 1000;

		//interes auxiliar
		InteresDTO interesAux = new InteresDTO();
		interesAux.interes = 10;


		//requests
		RequestBuilder requestDeposito = MockMvcRequestBuilders.post("/deposito").contentType(APPLICATION_JSON).content(ow.writeValueAsString(transaccionAux));
		RequestBuilder requestExtraccion = MockMvcRequestBuilders.post("/extraccion").contentType(APPLICATION_JSON).content(ow.writeValueAsString(transaccionAux));
		RequestBuilder requestSaldo = MockMvcRequestBuilders.get("/saldo");
		RequestBuilder requestInteres = MockMvcRequestBuilders.post("/interes").contentType(APPLICATION_JSON).content(ow.writeValueAsString(interesAux));

		// Deposito 1, saldo actual: 1000
		this.mvc.perform(requestDeposito).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.monto").value(1000));
		System.out.println("durante la prueba");



		//2do deposito, saldo actual: 2000
		this.mvc.perform(requestDeposito).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.monto").value(1000));


		//1er interés, monto esperado: 200, saldo luego: 2200
		this.mvc.perform(requestInteres).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.monto").value(200));


		//1ra consulta de saldo
		this.mvc.perform(requestSaldo).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value(2200));

		System.out.println("durante la prueba");
	}

	@AfterEach
	void tearDown() {
	}


}
