package bordero.backend.controller;

import bordero.backend.model.Identificable;
import bordero.backend.service.CRUDService;
import bordero.backend.service.Response;
import bordero.dto.DTO;
import bordero.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

public abstract class CRUDRestController<T extends DTO, M extends Identificable> {

    private CRUDService<M> service;

    private DTOModelMapper<T, M> mapper;

    public CRUDRestController(CRUDService<M> service,
                              DTOModelMapper imapper) {
        this.setService(service);
        this.setMapper(imapper);
    }

    protected abstract String url();

   @PostMapping("")
   public ResponseEntity<ResponseDTO<T>> insert(@RequestBody T inDTO) {
       ResponseDTO<T> outDTO = executeInsert(inDTO);
       if (outDTO.isValid()) {
           return ResponseEntity
                   .created(URI.create(url() + outDTO.value.id)).body(outDTO);
       } else {
           return ResponseEntity.badRequest().body(outDTO);
       }
   }

    public ResponseDTO<T> executeInsert(T inDTO) {
        M prev = getMapper().toModel(inDTO);
        Response<M> result = getService().insert(prev);
        return getMapper().toDTO(result);
    }


    @GetMapping("{id}")
    public ResponseEntity<ResponseDTO<T>> findById(@PathVariable long id) {
        ResponseDTO<T> outDTO = executeFindById(id);
        if (outDTO.isValid()) return ResponseEntity.ok().body(outDTO);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outDTO);
    }

    public ResponseDTO<T> executeFindById(long id) {
        Response<M> result = getService().findById(id);
        return getMapper().toDTO(result);
    }

    @GetMapping("")
    public ResponseEntity<List<T>> getAll(@RequestParam Map<String, String> params) {
        List<T> outDTO = executeGetAll(params);
        return ResponseEntity.ok().body(outDTO);
    }

    public List<T> executeGetAll(Map<String, String> params) {
        List<M> models = null;
        if (params.isEmpty()) models = getService().findAll();
        else models = query(params);
        return getMapper().toDTO(models);
    }

    @GetMapping(value = "", params = "parentId")
    public ResponseEntity<List<T>> findChildrenForParent(@RequestParam("parentId") long parentId) {
        List<M> models = getService().findChildrenForParent(parentId);
        List<T> outDTO = getMapper().toDTO(models);
        return ResponseEntity.ok().body(outDTO);
    }


    protected List<M> query(Map<String, String> params) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<T>> update(@RequestBody T inDTO) {
        ResponseDTO<T> outDTO = executeUpdate(inDTO);
        if (outDTO.isValid()) {
            return ResponseEntity.ok(outDTO);
        } else {
            return ResponseEntity.badRequest().body(outDTO);
        }
    }

    public ResponseDTO<T> executeUpdate(T inDTO) {
        M model = getMapper().toModel(inDTO);
        Response<M> result = getService().update(model);
        return getMapper().toDTO(result);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<ResponseDTO<T>> delete(@PathVariable("id") long id) {
        ResponseDTO<T> outDTO = executeDelete(id);
        if (outDTO.isValid()) {
            return ResponseEntity.ok().body(outDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outDTO);

        }
    }

    public ResponseDTO<T> executeDelete(long id) {
        Response<M> result = getService().delete(id);
        return getMapper().toDTO(result);
    }

    public CRUDService<M> getService() {
        return service;
    }

    public void setService(CRUDService<M> service) {
        this.service = service;
    }

    public DTOModelMapper<T, M> getMapper() {
        return mapper;
    }

    public void setMapper(DTOModelMapper<T, M> mapper) {
        this.mapper = mapper;
    }
}
