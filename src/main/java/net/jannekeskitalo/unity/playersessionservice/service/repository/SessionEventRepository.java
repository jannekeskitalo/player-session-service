package net.jannekeskitalo.unity.playersessionservice.service.repository;

import net.jannekeskitalo.unity.playersessionservice.service.repository.model.SessionEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionEventRepository extends CassandraRepository<SessionEvent> {

}
