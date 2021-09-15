package bordero.backend.service;

import bordero.backend.model.Performance;

public interface PerformanceService extends CRUDService<Performance> {

    int ERROR_PERFORMANCE_UNSPECIFIED = 4001;
    int ERROR_PERFORMANCE_PLAY_MISSING = 4002;

}
