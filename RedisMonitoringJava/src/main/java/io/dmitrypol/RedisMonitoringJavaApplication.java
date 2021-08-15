package io.dmitrypol;

import io.dmitrypol.client.RedisSentinelClient;
import io.dmitrypol.resources.DevOpsResource;
import io.dropwizard.Application;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.redis.RedisClusterClientBundle;
import io.dropwizard.redis.RedisClusterClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import lombok.var;

public class RedisMonitoringJavaApplication extends Application<RedisMonitoringJavaConfiguration> {

    public static void main(final String[] args) throws Exception {
        new RedisMonitoringJavaApplication().run(args);
    }

    @Override
    public String getName() {
        return "RedisMonitoringJava";
    }

    @Override
    public void initialize(final Bootstrap<RedisMonitoringJavaConfiguration> bootstrap) {
        bootstrap.addBundle(redisClientBundle);
        bootstrap.addBundle(redisClusterClientBundle);
        bootstrap.addBundle(new ViewBundle<>());
    }

    @Override
    public void run(final RedisMonitoringJavaConfiguration config, final Environment env) {
        final var redisConnection = redisClientBundle.getConnection();
        final var redisClusterConnection = redisClusterClientBundle.getClusterConnection();
        final var redisSentinelClient = new RedisSentinelClient(config);
        env.jersey().register(new DevOpsResource(redisSentinelClient, redisConnection,redisClusterConnection));
    }

    private final RedisClientBundle<String, String, RedisMonitoringJavaConfiguration> redisClientBundle = new RedisClientBundle<String, String, RedisMonitoringJavaConfiguration>() {
        @Override
        public RedisClientFactory<String, String> getRedisClientFactory(RedisMonitoringJavaConfiguration config) {
            return config.getRedisClientFactory();
        }
    };

    private final RedisClusterClientBundle<String, String, RedisMonitoringJavaConfiguration> redisClusterClientBundle = new RedisClusterClientBundle<String, String, RedisMonitoringJavaConfiguration>() {
        @Override
        public RedisClusterClientFactory<String, String> getRedisClusterClientFactory(RedisMonitoringJavaConfiguration config) {
            return config.getRedisClusterClientFactory();
        }
    };
}
