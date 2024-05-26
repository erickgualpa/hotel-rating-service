package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMqPublicEventBus implements PublicEventBus {

    private final EntityManager entityManager;

    private final String rabbitMqHost;
    private final int rabbitMqAmqpPort;
    private final String rabbitMqAdminUsername;
    private final String rabbitMqAdminPassword;

    public RabbitMqPublicEventBus(
            EntityManager entityManager,
            String rabbitMqHost,
            int rabbitMqAmqpPort,
            String rabbitMqAdminUsername,
            String rabbitMqAdminPassword
    ) {
        this.entityManager = entityManager;
        this.rabbitMqHost = rabbitMqHost;
        this.rabbitMqAmqpPort = rabbitMqAmqpPort;
        this.rabbitMqAdminUsername = rabbitMqAdminUsername;
        this.rabbitMqAdminPassword = rabbitMqAdminPassword;
    }

    @Transactional
    @Override
    public void publish(List<DomainEvent> events) {
        /*String sql = """
                    INSERT INTO event_store(aggregate_id, occurred_on, event_type)
                    VALUES (:aggregateId, :occurredOn, :eventType)
                """;
        events.forEach(
                e -> entityManager.createNativeQuery(sql)
                        .setParameter("aggregateId", UUID.fromString(e.getAggregateId().value()))
                        .setParameter("occurredOn", e.getOccurredOn())
                        .setParameter("eventType", e.getType())
                        .executeUpdate()
        );*/

        // TODO: Implement RabbitMQ publish logic
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqHost);
        factory.setPort(rabbitMqAmqpPort);
        factory.setUsername(rabbitMqAdminUsername);
        factory.setPassword(rabbitMqAdminPassword);

        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        try (Channel channel = connection.createChannel()) {
            channel.queueDeclare("hotelmanagement.reviews", false, false, false, null);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
