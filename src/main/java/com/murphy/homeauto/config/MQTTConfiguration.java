package com.murphy.homeauto.config;

import com.murphy.homeauto.model.PlugMqttMessage;
import com.murphy.homeauto.service.SmartPlugService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Configuration
public class MQTTConfiguration {

    @Value("${mqtt.server}")
    String mqttServer;

    @Value("${mqtt.user}")
    String mqttUser;

    @Value("${mqtt.password}")
    String mqttPassword;

    @Value("${mqtt.topic.smartplug}")
    String smartPlugTopic;

    @Resource
    SmartPlugService smartPlugService;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { mqttServer });
        options.setUserName(mqttUser);
        options.setPassword(mqttPassword.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageProducer inbound() {

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttServer,
                        "homeautoClient", mqttClientFactory(), smartPlugTopic);

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @Filter(inputChannel = "mqttInputChannel", outputChannel = "filteredMessages")
    public MessageSelector filterInvalidMessages(){
        return new MessageSelector() {
            @Override
            public boolean accept(Message<?> message) {
                return message.getPayload() != null && !StringUtils.startsWithIgnoreCase("online", (String) message.getPayload());
            }
        };
    }

    @Bean
    @Transformer(inputChannel="filteredMessages", outputChannel="toHandler")
    JsonToObjectTransformer jsonToObjectTransformer() {
        return new JsonToObjectTransformer(PlugMqttMessage.class);
    }

    @Bean
    @ServiceActivator(inputChannel = "toHandler")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                PlugMqttMessage plugMqttMessage = (PlugMqttMessage) message.getPayload();
                if(plugMqttMessage.getPlugEnergy() != null){
                    smartPlugService.saveEnergyReading("Plug 1", plugMqttMessage.getPlugEnergy());
                }
            }

        };
    }

}
