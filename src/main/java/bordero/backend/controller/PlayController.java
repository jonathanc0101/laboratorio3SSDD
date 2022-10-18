package bordero.backend.controller;

import bordero.backend.kafka.Producer;
import bordero.backend.model.Play;
import bordero.backend.service.PlayService;
import bordero.backend.service.Response;
import bordero.dto.PlayDTO;
import bordero.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/plays", produces = "application/json")
public class PlayController extends CRUDRestController<PlayDTO, Play> {
    @Autowired
    Producer<PlayDTO> producer;
    public PlayController(PlayService service, DTOModelMapper<PlayDTO, Play> mapper) {
        super(service, mapper);
    }

    @Override
    protected String url() {
        return "/plays/";
    }

    @Override
    @PostMapping("")
    public ResponseEntity<ResponseDTO<PlayDTO>> insert(@RequestBody PlayDTO inDTO) {
        ResponseDTO<PlayDTO> outDTO = executeInsert(inDTO);
        if (!outDTO.isValid()) return ResponseEntity.badRequest().body(outDTO);
        producer.logCreate("plays", inDTO);
        return ResponseEntity
                .created(URI.create(url() + outDTO.value.id)).body(outDTO);
    }

    @GetMapping(value = "", params = "code")
    public ResponseEntity<ResponseDTO<PlayDTO>> findOne(@RequestParam("code") String code) {
        Play example = new Play();
        example.setCode(code);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id");
        Response<Play> out = getService().findOne(Example.of(example, matcher));
        ResponseDTO<PlayDTO> outDTO = getMapper().toDTO(out);
        if (outDTO.isValid()) return ResponseEntity.ok().body(outDTO);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outDTO);
    }

    @GetMapping(value = "", params = {"page", "size"})
    public ResponseEntity<Page<PlayDTO>> getPage(Pageable pagReq) {
        Page<Play> out = getService().getPage(pagReq);
        Page<PlayDTO> outDTO = getMapper().toDTO(out);
        return ResponseEntity.ok().body(outDTO);
    }


}
