package bordero.backend.controller;

import bordero.backend.service.Response;
import bordero.dto.DTO;
import bordero.dto.ResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DTOModelMapper<T extends DTO, M> {

    T toDTO(M model);

    M toModel(T dto);

    List<T> toDTO(List<M> models);

    Page<T> toDTO(Page<M> models);


    ResponseDTO<T> toDTO(Response<M> response);

}
