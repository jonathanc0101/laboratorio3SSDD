package bordero.backend;

import bordero.backend.service.CRUDService;
import bordero.client.RestCRUDClient;
import bordero.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ITBorderoCRUD {

    @LocalServerPort
    private int port;

    private RestCRUDClient<CustomerDTO> custCli;
    private RestCRUDClient<BorderoDTO> bordCli;

    BorderoDTO aBordero = null;
    CustomerDTO aCustomer = null;
    LocalDate date = null;

    public String baseURL() {
        return "http://localhost:" + port + "/api/borderos/";
    }

    public String customersURL() {
        return "http://localhost:" + port + "/api/customers/";
    }

    List<BorderoDTO> borderos;

    @Test
    @Order(1)
    public void insertABordero() {
        aBordero = new BorderoDTO(date, aCustomer);
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.create(aBordero);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        aBordero = res.getBody().value;
        assertNotEquals(0, aBordero.id);
        assertEquals(date, aBordero.date);
        assertEquals(aCustomer.name, aBordero.customer.name);
    }

    @Test
    @Order(2)
    public void insertEmptyBordero() {
        BorderoDTO empty = new BorderoDTO();
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.create(empty);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(10)
    public void findABordero() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.findById(aBordero.id);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        BorderoDTO retrieved = res.getBody().value;
        assertNotEquals(0, retrieved.id);
        assertEquals(date, retrieved.date);
        assertEquals(aCustomer.name, retrieved.customer.name);
    }

    @Test
    @Order(11)
    public void findNonExistentPerformance() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.findById(10);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }

    @Test
    @Order(12)
    public void findAll() {
        ResponseEntity<List<BorderoDTO>> res = bordCli.getall();
        List<BorderoDTO> list = res.getBody();
        ListIterator<BorderoDTO> it = list.listIterator();
        while (it.hasNext()) {
            BorderoDTO dto = it.next();
            System.out.println("bordero id: " + dto.id + " date: " + dto.date + " customer: " + dto.customer.name);
        }
        assertEquals(2, list.size());
    }

    @Test
    @Order(13)
    public void findPage() {
        PageRequest pageReq = PageRequest.of(0, 5, Sort.unsorted());
        ResponseEntity<Page<BorderoDTO>> res = bordCli.getPage(pageReq);
        int total = 10;
        if (aBordero == null)
            total = 7;
        Page page = res.getBody();
        assertEquals(0, page.getNumber());
        assertEquals(1, page.getTotalPages());
        assertEquals(2, page.getNumberOfElements());
        assertEquals(2, page.getTotalElements());
        assertEquals(false, page.getSort().isSorted());
    }

    @Test
    @Order(20)
    public void updateABordero() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli
                .update(new BorderoDTO(aBordero.id, date.plusDays(3), aCustomer));
        assertEquals(HttpStatus.OK, res.getStatusCode());
        aBordero = res.getBody().value;
        assertEquals(date.plusDays(3), aBordero.date);
    }

    @Test
    @Order(21)
    public void updateNonExistentPerformance() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.update(new BorderoDTO());
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(CRUDService.ERROR_INVALID_IDENTIFIER, res.getBody().code);
    }

    @Test
    @Order(21)
    public void updateNullPerformance() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.update(null);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(30)
    public void deleteABordero() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.delete(aBordero.id);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteNonExistentBordero() {
        ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.delete(10000);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals(CRUDService.ERROR_NOT_FOUND, res.getBody().code);
    }

    @BeforeAll
    public void createContext() {
        System.out.println("Before all");
        custCli = new RestCRUDClient<>();
        custCli.setClazz(CustomerDTO.class);
        custCli.setUrl(customersURL());

        bordCli = new RestCRUDClient<>();
        bordCli.setClazz(BorderoDTO.class);
        bordCli.setUrl(baseURL());

        aCustomer = custCli.create(new CustomerDTO("Compañía de Teatro")).getBody().value;

        date = LocalDate.of(2020, 03, 1);

        borderos = new ArrayList<>();
        borderos.add(new BorderoDTO(date, aCustomer));

        borderos = borderos.stream().map(bord -> {
            ResponseDTO<BorderoDTO> res = bordCli.create(bord).getBody();
            return res.value;
        }).collect(Collectors.toList());

        // Iterator<BorderoDTO> it = borderos.iterator();
        // while (it.hasNext()) {
        // BorderoDTO bor = it.next();
        // ResponseDTO<BorderoDTO> res = bordCli.create(bor).getBody();
        // }

    }

    @AfterAll
    public void destroyContext() {
        System.out.println("After all");
        Iterator<BorderoDTO> it = borderos.iterator();
        while (it.hasNext()) {
            BorderoDTO bor = it.next();
            ResponseEntity<ResponseDTO<BorderoDTO>> res = bordCli.delete(bor.id);
            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
        ResponseEntity<ResponseDTO<CustomerDTO>> del = custCli.delete(aCustomer.id);
        assertEquals(HttpStatus.OK, del.getStatusCode());
    }

}
