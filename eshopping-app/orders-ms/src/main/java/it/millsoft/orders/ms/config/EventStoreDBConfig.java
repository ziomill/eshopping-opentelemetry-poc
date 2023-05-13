package it.millsoft.orders.ms.config;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {

    @Value("${eventstoredb.uri}")
    private String eventStoreDbUri;

    @Bean
    public EventStoreDBClient createEventStoreDbClient() throws Exception {
        EventStoreDBClientSettings settings = EventStoreDBConnectionString.parse(eventStoreDbUri);
        EventStoreDBClient client = EventStoreDBClient.create(settings);
        return client;
    }

}
