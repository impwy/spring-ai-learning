package com.backai;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.backai.ai.service.ChatService;

@SpringBootTest
@ActiveProfiles("test")
class BackAiApplicationTests {

    @Autowired
    private ChatService chatService; // 테스트할 ChatService 빈 주입

    @Test
    @DisplayName("Ollama Chat 클라이언트 테스트") // 테스트 이름 지정
    void t1(){
        assertDoesNotThrow(()->{ // 예외가 발생하지 않아야 테스트 통과

            String response = chatService.streamChatResponse("한국말해봐")
                                         // Ollama에 메시지 전송, Flux<String> 반환 (스트리밍 응답)

                                         .collectList()
                                         // Flux의 모든 요소를 List로 수집 → Mono<List<String>>

                                         .map(list -> String.join("", list))
                                         // 리스트의 문자열들을 하나로 합침 → Mono<String>

                                         .block();
            // 비동기 → 동기 변환, 결과가 올 때까지 대기

            assertNotNull(response); // 응답이 null이 아닌지 확인
            assertFalse(response.isEmpty()); // 응답이 비어있지 않은지 확인
            System.out.println("Ollama Response: " + response); // 결과 출력
        });
    }

}
