package bordero.backend;

import bordero.backend.service.CRUDService;
import bordero.backend.service.PerformanceService;
import bordero.client.RestCRUDClient;
import bordero.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ITPerformanceCRUD {

    @LocalServerPort
    private int port;

    private RestCRUDClient<PlayDTO> playCli;
    private RestCRUDClient<BorderoDTO> bordCli;
    private RestCRUDClient<CustomerDTO> custCli;
    private RestCRUDClient<PerformanceDTO> perfCli;


    public String baseURL() {
        return "http://localhost:" + port + "/api/performances/";
    }

    public String playsURL() {
        return "http://localhost:" + port + "/api/plays/";
    }

    public String customersURL() {
        return "http://localhost:" + port + "/api/customers/";
    }

    public String borderoURL() {
        return "http://localhost:" + port + "/api/borderos/";
    }


    BorderoDTO aBordero = null;
    CustomerDTO aCustomer = null;

    PerformanceDTO aPerf = null;

    PlayDTO hamlet = null;
    PlayDTO othello = null;

    @Test
    @Order(1)
    public void insert() {
        int audience = 55;
        aPerf = new PerformanceDTO(audience, hamlet, aBordero);
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.create(aPerf);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        aPerf = res.getBody().value;
        assertEquals(audience, aPerf.audience);
        assertEquals(hamlet.code, aPerf.play.code);
        assertNotEquals(0, aPerf.id);
    }

    @Test
    @Order(2)
    public void insertNoPlayPerformance() {
        PerformanceDTO noname = new PerformanceDTO();
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.create(noname);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(PerformanceService.ERROR_PERFORMANCE_PLAY_MISSING, res.getBody().code);
    }


    @Test
    @Order(2)
    public void insertNullPerformance() {
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.create(null);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(10)
    public void find() {
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.findById(aPerf.id);
        PerformanceDTO retrieved = res.getBody().value;
        assertNotEquals(0, retrieved.id);
        assertEquals(aPerf.play.code, retrieved.play.code);
        assertEquals(aPerf.audience, retrieved.audience);
    }

    @Test
    @Order(11)
    public void findNonExistenPerformance() {
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.findById(10);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(12)
    public void findAll() {
        ResponseEntity<List<PerformanceDTO>> res = perfCli.getall();
        int count = res.getBody().size();
        assertEquals(1, count);
    }

    @Test
    @Order(13)
    public void findPerformancesForBordero() {
        HashMap<String, String> params = new HashMap<>();
        params.put("parentId", Long.toString(aBordero.id));
        ResponseEntity<List<PerformanceDTO>> res = perfCli.query(params);
        List<PerformanceDTO> retrieved = res.getBody();
        int count = retrieved.size();
        assertEquals(1, count);
    }


    @Test
    @Order(22)
    public void update() {
        aPerf = new PerformanceDTO(aPerf.id, 65, othello, aPerf.bordero);
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.update(aPerf);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        aPerf = res.getBody().value;
        assertEquals(othello.code, aPerf.play.code);
        assertEquals(65, aPerf.audience);
    }

    @Test
    @Order(23)
    public void updateNonExistent() {
        PerformanceDTO nonExistent = new PerformanceDTO(10000, 65, othello, aBordero);
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.update(nonExistent);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }


    @Test
    @Order(30)
    public void deleteAPerformance() {
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.delete(aPerf.id);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteANonExistenPerformance() {
        ResponseEntity<ResponseDTO<PerformanceDTO>> res = perfCli.delete(10);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }

    @BeforeAll
    public void createContext() {
        playCli = new RestCRUDClient<PlayDTO>();
        playCli.setClazz(PlayDTO.class);
        playCli.setUrl(playsURL());

        perfCli = new RestCRUDClient<>();
        perfCli.setClazz(PerformanceDTO.class);
        perfCli.setUrl(baseURL());

        hamlet = new PlayDTO("hamlet", "Hamlet", "tragedy");
        ResponseEntity<ResponseDTO<PlayDTO>> res = playCli.create(hamlet);
        hamlet = res.getBody().value;

        othello = playCli
                .create(new PlayDTO("othello", "Othello", "tragedy"))
                .getBody().value;

        custCli = new RestCRUDClient<>();
        custCli.setClazz(CustomerDTO.class);
        custCli.setUrl(customersURL());

        aCustomer = custCli
                .create(new CustomerDTO("Compañía de Teatro"))
                .getBody().value;

        bordCli = new RestCRUDClient<>();
        bordCli.setClazz(BorderoDTO.class);
        bordCli.setUrl(borderoURL());

        aBordero = new BorderoDTO(LocalDate.of(2020, 03, 1), aCustomer);
        ResponseEntity<ResponseDTO<BorderoDTO>> resBord = bordCli.create(aBordero);
        aBordero = resBord.getBody().value;

    }

    @AfterAll
    public void destroyContext() {
        bordCli.delete(aBordero.id);
        custCli.delete(aCustomer.id);
        playCli.delete(hamlet.id);
        playCli.delete(othello.id);
    }


}
