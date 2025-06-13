package board.backend.auth.infra.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

interface SessionEntityRepository extends KeyValueRepository<SessionEntity, String> {

    void deleteByUserId(Long userId);

}
