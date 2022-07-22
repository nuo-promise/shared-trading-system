package cn.suparking.customer.configuration;

import cn.suparking.customer.configuration.properties.ParkmqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;

@Configuration("ParkMqConfiguration")
public class ParkMqConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ParkMqConfiguration.class);

    private final ParkConsumer parkConsumer;

    @Resource
    private ParkmqProperties parkmqProperties;

    public ParkMqConfiguration(final ParkConsumer parkConsumer) {
        this.parkConsumer = parkConsumer;
    }

    /**
     * MQ Factory.
     * @return {@link CachingConnectionFactory}
     */
    @Bean("ParkMQCloudConnectionFactory")
    public CachingConnectionFactory cloudConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                parkmqProperties.getHost(), parkmqProperties.getPort());
        connectionFactory.setUsername(parkmqProperties.getUserName());
        connectionFactory.setPassword(parkmqProperties.getPassWord());

        connectionFactory.addConnectionListener(new ConnectionListener() {

            @Override
            public void onCreate(final Connection connection) {
                LOG.info("Park MQ Cloud Connection is created");
            }

            @Override
            public void onShutDown(final ShutdownSignalException signal) {
                LOG.warn("Park MQ Cloud Connection is shutdown due to " + signal.getMessage());
            }
        });

        connectionFactory.addChannelListener(new ChannelListener() {

            @Override
            public void onCreate(final Channel channel, final boolean b) {
                LOG.info("Park MQ Cloud Channel is created");
            }

            public void onShutDown(final ShutdownSignalException signal) {
                LOG.warn("Park MQ Cloud Channel is shutdown due to " + signal.getMessage());
            }
        });
        return connectionFactory;
    }

    /**
     * cloud template.
     * @param factory {@link CachingConnectionFactory}
     * @return {@link RabbitTemplate}
     */
    @Bean("ParkMQCloudTemplate")
    public RabbitTemplate mqCloudTemplate(@Qualifier("ParkMQCloudConnectionFactory") final CachingConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    /**
     * Cloud Admin.
     * @param template {@link RabbitTemplate}
     * @return {@link AmqpAdmin}
     */
    @Bean("ParkMQCloudAMQPAdmin")
    public AmqpAdmin cloudAmqpAdmin(@Qualifier("ParkMQCloudTemplate") final RabbitTemplate template) {
        RabbitAdmin admin = new RabbitAdmin(template);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
        admin.setRetryTemplate(retryTemplate);
        return admin;
    }

    /**
     * cloud exchange.
     * @param admin {@link AmqpAdmin}
     * @return {@link TopicExchange}
     */
    @Bean("ParkMQCloudExchange")
    public TopicExchange cloudExchange(@Qualifier("ParkMQCloudAMQPAdmin")final AmqpAdmin admin) {
        TopicExchange exchange = new TopicExchange(parkmqProperties.getExchange());
        exchange.setShouldDeclare(false);
        return exchange;
    }

    /**
     * cloud Queue.
     * @param admin {@link AmqpAdmin}
     * @return {@link Queue}
     */
    @Bean("ParkMQCloudQueue")
    public Queue cloudQueue(@Qualifier("ParkMQCloudAMQPAdmin") final AmqpAdmin admin) {
        String queueName = "shared.customer1.payLib";
        Queue queue = new Queue(queueName, false, true, true);
        queue.setAdminsThatShouldDeclare(admin);
        queue.setShouldDeclare(true);
        return queue;
    }

    /**
     * cloud binding.
     * @param admin {@link AmqpAdmin}
     * @param queue {@link Queue}
     * @param exchange {@link TopicExchange}
     * @return {@link Binding}
     */
    @Bean("ParkMQCloudBinding")
    public Binding cloudBinding(@Qualifier("ParkMQCloudAMQPAdmin")final AmqpAdmin admin,
                                @Qualifier("ParkMQCloudQueue")final Queue queue,
                                @Qualifier("ParkMQCloudExchange")final TopicExchange exchange) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("#.pay_config");
        binding.setAdminsThatShouldDeclare(admin);
        binding.setShouldDeclare(true);
        return binding;
    }


    /**
     * cloud container.
     * @param connectionFactory {@link CachingConnectionFactory}
     * @param admin {@link AmqpAdmin}
     * @param queue {@link Queue}
     * @return {@link DirectMessageListenerContainer}
     */
    @Bean("ParkMQCloudMessageListenerContainer")
    @ConditionalOnProperty(name = "spring.parkmq.enable", matchIfMissing = true)
    public DirectMessageListenerContainer cloudMessageListenerContainer(
            @Qualifier("ParkMQCloudConnectionFactory")final CachingConnectionFactory connectionFactory,
            @Qualifier("ParkMQCloudAMQPAdmin")final AmqpAdmin admin,
            @Qualifier("ParkMQCloudQueue")final Queue queue
    ) {
        DirectMessageListenerContainer container = new DirectMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setAmqpAdmin(admin);
        container.setQueueNames(queue.getName());
        container.setPrefetchCount(parkmqProperties.getConsumerPrefetch());
        container.setConsumersPerQueue(parkmqProperties.getConcurrentConsumer());
        container.setAcknowledgeMode(AcknowledgeMode.NONE);
        container.setMessageListener(parkConsumer);
        return container;
    }
}
