package net.jannekeskitalo.unity.playersessionservice.ingest;

import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionInfoRepository extends CassandraRepository<SessionInfo, UUID> {

    Optional<SessionInfo> findBySessionId(UUID sessionId);

    void save(Iterable<SessionInfo> sessions);

    SessionInfo save(SessionInfo session);
}
