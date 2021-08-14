package io.dmitrypol;

import io.dmitrypol.resources.RedisMonitoringResource;
import io.dropwizard.Application;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lettuce.core.api.StatefulRedisConnection;

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
        bootstrap.addBundle(redis);
    }

    @Override
    public void run(final RedisMonitoringJavaConfiguration config, final Environment env) {
        final StatefulRedisConnection<String, String> redisConnection = redis.getConnection();
        env.jersey().register(new RedisMonitoringResource(redisConnection));
    }

    private final RedisClientBundle<String, String, RedisMonitoringJavaConfiguration> redis = new RedisClientBundle<String, String, RedisMonitoringJavaConfiguration>() {
        @Override
        public RedisClientFactory<String, String> getRedisClientFactory(RedisMonitoringJavaConfiguration config) {
            return config.getRedisClientFactory();
        }
    };
}
