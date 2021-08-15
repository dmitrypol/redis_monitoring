package io.dmitrypol;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.redis.RedisClusterClientFactory;
import lombok.Getter;
import org.hibernate.validator.constraints.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Getter
public class RedisMonitoringJavaConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisClientFactory<String, String> redisClientFactory;

    @Valid
    @NotNull
    @JsonProperty("redis-cluster")
    private RedisClusterClientFactory<String, String> redisClusterClientFactory;

    @Valid
    @NotNull
    @JsonProperty("sentinels")
    private List<Map<String, String>> sentinels;

}
