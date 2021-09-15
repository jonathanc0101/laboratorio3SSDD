package bordero.backend.controller;

import bordero.backend.model.Play;
import bordero.backend.service.PlayService;
import bordero.backend.service.Response;
import bordero.dto.PlayDTO;
import bordero.dto.ResponseDTO;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/plays", produces = "application/json")
public class PlayController extends CRUDRestController<PlayDTO, Play> {

    public PlayController(PlayService service, DTOModelMapper<PlayDTO, Play> mapper) {
        super(service, mapper);
    }

    @Override
    protected String url() {
        return "/plays/";
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
