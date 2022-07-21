package cn.suparking.data.configuration;

import cn.suparking.data.Application;
import cn.suparking.data.configuration.properties.RabbitmqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.enable", matchIfMissing = true)
public class RabbitMqConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqConfiguration.class);

    private final MessageListener deviceReceive;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public RabbitMqConfiguration(@Qualifier("DeviceReceive")final MessageListener deviceReceive) {
        this.deviceReceive = deviceReceive;
    }

    /**
     * MQ Factory.
     * @return {@link CachingConnectionFactory}
     */
    @Bean("MQCloudConnectionFactory")
    public CachingConnectionFactory cloudConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                rabbitmqProperties.getHost(), rabbitmqProperties.getPort());
        connectionFactory.setUsername(rabbitmqProperties.getUserName());
        connectionFactory.setPassword(rabbitmqProperties.getPassWord());

        connectionFactory.addConnectionListener(new ConnectionListener() {
            @Override
            public void onCreate(final Connection connection) {
                LOG.info("MQ cloud connection is created");
            }

            @Override
            public void onShutDown(final ShutdownSignalException signal) {
                LOG.warn("MQ cloud connection is shutdown due to " + signal.getMessage());
            }
        });
        connectionFactory.addChannelListener(new ChannelListener() {

            @Override
            public void onCreate(final Channel channel, final boolean b) {
                LOG.info("MQ cloud channel is created");
            }

            @Override
            public void onShutDown(final ShutdownSignalException signal) {
                LOG.warn("MQ cloud channel is shutdown due to " + signal.getMessage());
            }
        });
        return connectionFactory;
    }

    /**
     * Cloud Admin.
     * @param template {@link RabbitTemplate}
     * @return {@link AmqpAdmin}
     */
    @Bean("MQCloudAMQPAdmin")
    public AmqpAdmin cloudAmqpAdmin(@Qualifier("MQCloudTemplate") final RabbitTemplate template) {
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
    @Bean("MQCloudExchange")
    public TopicExchange cloudExchange(@Qualifier("MQCloudAMQPAdmin")final AmqpAdmin admin) {
        TopicExchange exchange = new TopicExchange(rabbitmqProperties.getExchange());
        exchange.setShouldDeclare(false);
        return exchange;
    }

    /**
     * cloud template.
     * @param factory {@link CachingConnectionFactory}
     * @return {@link RabbitTemplate}
     */
    @Bean("MQCloudTemplate")
    public RabbitTemplate mqCloudTemplate(@Qualifier("MQCloudConnectionFactory") final CachingConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    /**
     * cloud Queue.
     * @param admin {@link AmqpAdmin}
     * @return {@link Queue}
     */
    @Bean("MQCloudQueue")
    public Queue cloudQueue(@Qualifier("MQCloudAMQPAdmin") final AmqpAdmin admin) {
        String queueName = "cs.device.data";
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
    @Bean("MQCloudBinding")
    public Binding cloudBinding(@Qualifier("MQCloudAMQPAdmin")final AmqpAdmin admin,
                                @Qualifier("MQCloudQueue")final Queue queue,
                                @Qualifier("MQCloudExchange")final TopicExchange exchange) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("*.device.#");
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
    @Bean("MQCloudMessageListenerContainer")
    @ConditionalOnProperty(name = "spring.rabbitmq.enable", matchIfMissing = true)
    public DirectMessageListenerContainer cloudMessageListenerContainer(
            @Qualifier("MQCloudConnectionFactory")final CachingConnectionFactory connectionFactory,
            @Qualifier("MQCloudAMQPAdmin")final AmqpAdmin admin,
            @Qualifier("MQCloudQueue")final Queue queue
    ) {
        DirectMessageListenerContainer container = new DirectMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setAmqpAdmin(admin);
        container.setQueueNames(queue.getName());
        container.setPrefetchCount(rabbitmqProperties.getConsumerPrefetch());
        container.setConsumersPerQueue(rabbitmqProperties.getConcurrentConsumer());
        container.setAcknowledgeMode(AcknowledgeMode.NONE);
        container.setMessageListener(deviceReceive);
        return container;
    }
}
