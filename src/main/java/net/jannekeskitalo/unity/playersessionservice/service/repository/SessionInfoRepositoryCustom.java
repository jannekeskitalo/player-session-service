package net.jannekeskitalo.unity.playersessionservice.service.repository;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.NewSessionEventRequest;
import net.jannekeskitalo.unity.playersessionservice.service.model.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SessionInfoRepositoryCustom {

    private final CassandraTemplate cassandraTemplate;

    public SessionInfoRepositoryCustom(@Autowired CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

    public void saveBatch(Iterable<SessionInfo> sessionInfos) {
        cassandraTemplate.batchOps().insert(sessionInfos).execute();
    }
}
