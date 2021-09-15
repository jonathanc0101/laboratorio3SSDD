package bordero.backend.service;

import bordero.backend.model.Play;
import bordero.backend.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlayServiceImpl implements PlayService {

    @Autowired
    private PlayRepository repository;

    @Override
    public JpaRepository<Play, Long> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public Response<Play> insert(Play p) {
        if (p != null) {
            if (p.getCode() != null && !p.getCode().isEmpty()) {
                Response<Play> found = this.findByCode(p.getCode());
                if (found.isValid()) return Response.ofError(PlayService.ERROR_CODE_ALREADY_IN_USE);
                else {
                    if (found.getCode() == PlayService.ERROR_PLAY_NOT_FOUND) {
                        p = repository.saveAndFlush(p);
                        return Response.ofValid(p);
                    } else return Response.ofError(PlayService.ERROR_UNKNOWN);
                }
            } else return Response.ofError(PlayService.ERROR_PLAY_CODE_MISSING);
        } else return Response.ofError(PlayService.ERROR_PLAY_UNSPECIFIED);
    }


    @Override
    public Response<Play> findByCode(String code) {
        Play example = new Play();
        example.setCode(code);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("name")
                .withIgnorePaths("type");
        Optional<Play> play;
        try {
            play = repository.findOne(Example.of(example, matcher));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Response.ofError(ERROR_MULTIPLE_PLAYS_FOUND);
        }
        return play.isPresent() ? Response.ofValid(play.get()) : Response.ofError(ERROR_PLAY_NOT_FOUND);
    }

}
