package io.dmitrypol;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.redis.RedisClientFactory;
import lombok.Getter;
import lombok.NonNull;
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

    @NotNull private List<Map<String, String>> sentinels;

}
