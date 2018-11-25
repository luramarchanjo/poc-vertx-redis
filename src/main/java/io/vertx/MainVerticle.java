package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static void main(String... arguments) {
    val vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start() {
    val redisOptions = new RedisOptions();
    redisOptions.setHost("127.0.0.1");

    val redisClient = RedisClient.create(vertx, redisOptions);
    val key = UUID.randomUUID().toString();
    val keyValue = UUID.randomUUID().toString();

    redisClient.append(key, keyValue, result -> {
      if (result.succeeded()) {
        log.info("Successfully inserted key {} with value {}", key, keyValue);
      } else {
        log.error("Error to insert key", result.cause());
      }
    });

    redisClient.get(key, result -> {
      if (result.succeeded()) {
        log.info("Successfully got key {} with value {}", key, result.result());
      } else {
        log.error("Error to get key", result.cause());
      }
    });

    redisClient.del(key, result -> {
      if (result.succeeded()) {
        log.info("Successfully deleted key {} with value {}", key, keyValue);
      } else {
        log.error("Error to delete key", result.cause());
      }
    });
  }

}