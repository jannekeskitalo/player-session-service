package net.jannekeskitalo.unity.playersessionservice.service.repository;

import net.jannekeskitalo.unity.playersessionservice.service.model.SessionInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.websocket.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionInfoRepository extends CassandraRepository<SessionInfo, UUID> {

    Optional<SessionInfo> findBySessionId(UUID sessionId);

    void save(Iterable<SessionInfo> sessions);

    SessionInfo save(SessionInfo session);
}
