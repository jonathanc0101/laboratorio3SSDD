package bordero.backend.service;

import bordero.backend.model.Play;

public interface PlayService extends CRUDService<Play> {

    int ERROR_PLAY_UNSPECIFIED = 3001;
    int ERROR_PLAY_CODE_MISSING = 3002;
    int ERROR_INVALID_IDENTIFIER = 3003;
    int ERROR_PLAY_NOT_FOUND = 3004;
    int ERROR_MULTIPLE_PLAYS_FOUND = 3005;
    int ERROR_CODE_ALREADY_IN_USE = 3006;

    Response<Play> findByCode(String code);

}
