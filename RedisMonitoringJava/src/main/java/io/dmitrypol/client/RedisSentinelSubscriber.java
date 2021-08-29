package io.dmitrypol.client;

import io.dmitrypol.RedisMonitoringJavaConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RedisSentinelSubscriber implements Managed {
    private List<StatefulRedisPubSubConnection<String, String>> sentClients = new ArrayList<>();

    public RedisSentinelSubscriber(RedisMonitoringJavaConfiguration config){
        for (Map<String, String> sentinel : config.getSentinels()) {
            String sentHost = sentinel.get("host");
            int sentPort = Integer.parseInt(sentinel.get("port"));
            RedisURI redisUri = RedisURI.Builder.redis(sentHost, sentPort).build();
            sentClients.add(RedisClient.create(redisUri).connectPubSub());
        }
    }

    @Override
    public void start() throws Exception {
        log.info("started RedisSentinelSubscriber");
        for (StatefulRedisPubSubConnection<String, String> sentClient : sentClients) {
            if (sentClient.sync().ping().equals("PONG")) {
                sentClient.addListener(getListener());
                sentClient.sync().psubscribe("*");
            }
        }
    }

    @Override
    public void stop() throws Exception {
        log.info("stopped RedisSentinelSubscriber");
    }

    private RedisPubSubListener<String, String> getListener() {
        RedisPubSubListener<String, String> listener =
                new RedisPubSubAdapter<String, String>() {
                    @Override
                    public void message(String pattern, String channel, String message) {
                        switch (channel) {
                            case "+odown":
                                log.info("+odown: {}", message);
                                break;
                            case "+sdown":
                                log.info("+sdown: {}", message);
                                break;
                            case "+switch-master":
                                log.info("+switch-master {}", message);
                                break;
                            default:
                                //log.info("pattern: {}, channel: {}: message: {}", pattern, channel, message);
                        }
                    }
                };
        return listener;
    }

}
