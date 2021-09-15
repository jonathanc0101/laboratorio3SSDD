package bordero.backend;

import bordero.backend.service.CRUDService;
import bordero.backend.service.CustomerService;
import bordero.client.RestCRUDClient;
import bordero.dto.CustomerDTO;
import bordero.dto.ResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ITCustomerCRUD {

    @LocalServerPort
    private int port;

    private RestCRUDClient<CustomerDTO> client;

    public String baseURL() {
        return "http://localhost:" + port + "/api/customers/";
    }

    CustomerDTO aCustomer = null;
    String name = "Teatro Uno";

    @Test
    @Order(1)
    public void insertACustomer() {
        aCustomer = new CustomerDTO(name);
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.create(aCustomer);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        aCustomer = res.getBody().value;
        assertEquals(name, aCustomer.name);
        assertNotEquals(0, aCustomer.id);
    }

    @Test
    @Order(2)
    public void insertNoNameCustomer() {
        CustomerDTO noname = new CustomerDTO();
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.create(noname);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(CustomerService.ERROR_CUSTOMER_NAME_MISSING, res.getBody().code);
    }


    @Test
    @Order(2)
    public void insertNullCustomer() {
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.create(null);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }


    @Test
    @Order(10)
    public void findACustomer() {
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.findById(aCustomer.id);
        CustomerDTO retrieved = res.getBody().value;
        assertEquals(aCustomer.name, retrieved.name);
        assertNotEquals(0, retrieved.id);
    }

    @Test
    @Order(13)
    public void findACustomerPorNombre() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", aCustomer.name.substring(0, 3));
        ResponseEntity<List<CustomerDTO>> res = client.query(params);
        List<CustomerDTO> retrieved = res.getBody();
        int count = retrieved.size();
        assertEquals(1, count);
    }


    @Test
    @Order(11)
    public void findNonExistenCustomer() {
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.findById(10);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(12)
    public void findAll() {
        ResponseEntity<List<CustomerDTO>> res = client.getall();
        int count = res.getBody().size();
        assertEquals(1, count);
    }

    @Test
    @Order(22)
    public void update() {
        String otroNombre = "Otro teatro";
        aCustomer = new CustomerDTO(aCustomer.id, otroNombre);
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.update(aCustomer);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        aCustomer = res.getBody().value;
        assertEquals(otroNombre, aCustomer.name);
    }

    @Test
    @Order(23)
    public void updateNonExistent() {
        CustomerDTO nonExistent = new CustomerDTO(10, "cualquiera");
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.update(nonExistent);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }


    @Test
    @Order(30)
    public void deleteACustomer() {
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.delete(aCustomer.id);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteANonExistenCustomer() {
        ResponseEntity<ResponseDTO<CustomerDTO>> res = client.delete(10);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }

    @BeforeAll
    public void initialize() {
        client = new RestCRUDClient<CustomerDTO>();
        client.setClazz(CustomerDTO.class);
        client.setUrl(baseURL());
    }

}
