package bordero.backend.controller;

import bordero.backend.service.Response;
import bordero.dto.DTO;
import bordero.dto.ResponseDTO;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class DozerDTOModelMapper<T extends DTO, M> implements DTOModelMapper<T, M> {

    @Autowired
    Mapper mapper;

    Class<T> dtoClazz;
    Class<M> modelClazz;

    public DozerDTOModelMapper(Class<T> dtoClazz, Class<M> modelClazz) {
        this.dtoClazz = dtoClazz;
        this.modelClazz = modelClazz;
    }

    public List<T> toDTO(List<M> models) {
        return models.stream()
                .map( m -> toDTO(m))
                .collect(Collectors.toList());
    }

    public Page<T> toDTO(Page<M> models) {
        List<T> dtos =  models.stream()
                .map( m -> toDTO(m))
                .collect(Collectors.toList());
        PageImpl<T> page = new PageImpl<>(dtos, models.getPageable(), models.getTotalElements());
        return page;
    }


    public M toModel(T dto) {
        return mapper.map(dto, modelClazz);
    }

    public T toDTO(M model) {
        return mapper.map(model, dtoClazz);
    }

    public ResponseDTO<T> toDTO(Response<M> response) {
        if (response.isValid()) {
            return new ResponseDTO<>(toDTO(response.get()));
        } else {
            return new ResponseDTO<>(response.getCode());
        }
    }

}
