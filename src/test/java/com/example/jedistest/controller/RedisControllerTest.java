package com.example.jedistest.controller;

import com.example.jedistest.controller.dto.RedisCrudSaveRequestDto;
import com.example.jedistest.domain.redis.RedisCrudRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisCrudRepository redisCrudRepository;

    @After
    public void tearDown() throws Exception {
        redisCrudRepository.deleteAll();
    }

    @Test
    public void 기본() {
        // given
        String url = "http://localhost:" + port;

        // when
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("ok");
    }

    @Test
    public void 기본_등록_조회() {
        // given
        RedisCrudSaveRequestDto requestDto = RedisCrudSaveRequestDto.builder()
                .id(1L)
                .description("description")
                .updatedAt(LocalDateTime.now())
                .build();

        String url = "http://localhost:" + port + "/save";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
    }

}
