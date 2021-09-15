package bordero.backend;

import bordero.backend.service.CRUDService;
import bordero.backend.service.PlayService;
import bordero.client.RestCRUDClient;
import bordero.dto.PlayDTO;
import bordero.dto.ResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ITPlayCRUD {

    @LocalServerPort
    private int port;

    private RestCRUDClient<PlayDTO> client;

    public String baseURL() {
        return "http://localhost:" + port + "/api/plays/";
    }

    PlayDTO aPlay = null;
    String code = "hamlet";
    String name = "Hamlet";
    String type = "tragedy";

    List<PlayDTO> plays;


    @BeforeAll
    public void initialize() {
        client = new RestCRUDClient<PlayDTO>();
        client.setClazz(PlayDTO.class);
        client.setUrl(baseURL());
        plays = Arrays.asList(new PlayDTO[]{
                new PlayDTO("as-like", "As You Like It", "comedy"),
                new PlayDTO("othello", "Othello", "tragedy"),
                new PlayDTO("otroplay1", "Otro Play 1", "tragedy"),
                new PlayDTO("otroplay2", "Otro Play 2", "comedy"),
                new PlayDTO("otroplay3", "Otro Play 3", "tragedy"),
                new PlayDTO("otroplay4", "Otro Play 4", "tragedy"),
                new PlayDTO("otroplay5", "Otro Play 5", "comedy"),
                new PlayDTO("otroplay6", "Otro Play 6", "comedy"),
                new PlayDTO("otroplay7", "Otro Play 7", "tragedy"),
        });
        plays = plays.stream()
                .map(play -> {
                    ResponseDTO<PlayDTO> res = client.create(play).getBody();
                    return res.value;
                }).collect(Collectors.toList());
    }

    @AfterAll
    public void dispose() {
        plays.stream().map(play -> client.delete(play.id)).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    public void insert() {
        aPlay = new PlayDTO(code, name, type);
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.create(aPlay);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        aPlay = res.getBody().value;
        assertEquals(name, aPlay.name);
        assertEquals(code, aPlay.code);
        assertEquals(type, aPlay.type);
        assertNotEquals(0, aPlay.id);
    }

    @Test
    @Order(2)
    public void insertNoNameCustomer() {
        PlayDTO noname = new PlayDTO();
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.create(noname);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(PlayService.ERROR_PLAY_CODE_MISSING, res.getBody().code);
    }


    @Test
    @Order(2)
    public void insertNullCustomer() {
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.create(null);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }


    @Test
    @Order(10)
    public void find() {
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.findById(aPlay.id);
        PlayDTO retrieved = res.getBody().value;
        assertEquals(aPlay.code, retrieved.code);
        assertEquals(aPlay.name, retrieved.name);
        assertEquals(aPlay.type, retrieved.type);
        assertNotEquals(0, retrieved.id);
    }

    @Test
    @Order(11)
    public void findNonExistenCustomer() {
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.findById(10000);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(12)
    public void findAll() {
        ResponseEntity<List<PlayDTO>> res = client.getall();
        int count = res.getBody().size();
        assertEquals(10, count);
    }

    @Test
    @Order(12)
    public void findByCode() {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", this.code);
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.findOne(params);
        PlayDTO retrieved = res.getBody().value;
        assertEquals(aPlay.code, retrieved.code);
        assertEquals(aPlay.name, retrieved.name);
        assertEquals(aPlay.type, retrieved.type);
        assertNotEquals(0, retrieved.id);
    }

    @Test
    @Order(13)
    public void findPage() {
        PageRequest pageReq = PageRequest.of(0, 5, Sort.by("name").descending());
        ResponseEntity<Page<PlayDTO>> res = client.getPage(pageReq);
        int total = 10;
        if (aPlay == null) total = 9;
        Page page = res.getBody();
        assertEquals(0, page.getNumber());
        assertEquals(2, page.getTotalPages());
        assertEquals(5, page.getNumberOfElements());
        assertEquals(total, page.getTotalElements());
        assertEquals(true, page.getSort().isSorted());
        pageReq = PageRequest.of(1, 5, Sort.by("name").descending());
        res = client.getPage(pageReq);
        page = res.getBody();
        assertEquals(1, page.getNumber());
        assertEquals(2, page.getTotalPages());
        assertEquals(5, page.getNumberOfElements());
        assertEquals(total, page.getTotalElements());
    }


    @Test
    @Order(22)
    public void update() {
        String aCode = "hamletB";
        String aName = "HamletB";
        String aType = "comedy";
        aPlay = new PlayDTO(aPlay.id, aCode, aName, aType);
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.update(aPlay);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        aPlay = res.getBody().value;
        assertEquals(aCode, aPlay.code);
        assertEquals(aName, aPlay.name);
        assertEquals(aType, aPlay.type);
    }

    @Test
    @Order(23)
    public void updateNonExistent() {
        PlayDTO nonExistent = new PlayDTO(10000, "cualquiera", "cualquiera", "none");
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.update(nonExistent);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }

    @Test
    @Order(30)
    public void deleteACustomer() {
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.delete(aPlay.id);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteANonExistenCustomer() {
        ResponseEntity<ResponseDTO<PlayDTO>> res = client.delete(10000);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }


}
