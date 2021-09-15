package bordero.backend.service;

import bordero.backend.model.Identificable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CRUDService<Model extends Identificable> {

    int ERROR_UNKNOWN = 1000;
    int ERROR_NOT_FOUND = 1001;
    int ERROR_INVALID_IDENTIFIER = 1002;
    int ERROR_MULTIPLE_ITEMS_FOUND = 1003;

    JpaRepository<Model, Long> getRepository();

    Response<Model> insert(Model p);

    default Response<Model> findById(long id) {
        Optional<Model> result = getRepository().findById(id);
        if (result.isPresent()) {
            return Response.ofValid(result.get());
        } else
            return Response.ofError(CRUDService.ERROR_NOT_FOUND);
    }

    default List<Model> findAll() {
        return getRepository().findAll();
    }

    default Response<Model> update(Model p) {
        if (p.getId() > 0) {
            if (getRepository().findById(p.getId()).isPresent()) {
                return Response.ofValid(getRepository().save(p));
            } else return Response.ofError(CRUDService.ERROR_NOT_FOUND);
        } else return Response.ofError(CRUDService.ERROR_INVALID_IDENTIFIER);
    }

    default Response<Model> delete(long id) {
        Optional<Model> result = getRepository().findById(id);
        if (result.isPresent()) {
            getRepository().delete(result.get());
            return Response.ofValid(result.get());
        } else
            return Response.ofError(CRUDService.ERROR_NOT_FOUND);
    }

    default Response<Model> findOne(Example<Model> example) {
        Optional<Model> item;
        try {
            item = getRepository().findOne(example);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Response.ofError(ERROR_MULTIPLE_ITEMS_FOUND);
        }
        return item.isPresent() ? Response.ofValid(item.get()) : Response.ofError(ERROR_NOT_FOUND);
    }

    default Page<Model> getPage(Pageable pagReq) {
        return getRepository().findAll(pagReq);
    }

    default List<Model> findChildrenForParent(long parentId) {
        throw new UnsupportedOperationException();
    }

    default List<Model> query(Example<Model> example) {
        return getRepository().findAll(example);
    }

}
