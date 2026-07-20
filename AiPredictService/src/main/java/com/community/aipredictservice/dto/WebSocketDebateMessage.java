package com.community.aipredictservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketDebateMessage {

    private String type;

    private Integer round;

    private Integer totalRounds;

    private String modelName;

    private String modelCode;

    private String content;

    private Long timestamp;

    private Boolean isRunning;

    private Object report;

    private String error;

    public static WebSocketDebateMessage start(String scenario, int totalRounds) {
        return WebSocketDebateMessage.builder()
                .type("START")
                .totalRounds(totalRounds)
                .isRunning(true)
                .build();
    }

    public static WebSocketDebateMessage roundStart(int round, int totalRounds) {
        return WebSocketDebateMessage.builder()
                .type("ROUND_START")
                .round(round)
                .totalRounds(totalRounds)
                .isRunning(true)
                .build();
    }

    public static WebSocketDebateMessage modelResponse(DebateMessage message, int totalRounds) {
        return WebSocketDebateMessage.builder()
                .type("MESSAGE")
                .round(message.getRound())
                .totalRounds(totalRounds)
                .modelName(message.getModelName())
                .modelCode(message.getModelCode())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .isRunning(true)
                .build();
    }

    public static WebSocketDebateMessage roundEnd(int round, int totalRounds) {
        return WebSocketDebateMessage.builder()
                .type("ROUND_END")
                .round(round)
                .totalRounds(totalRounds)
                .isRunning(true)
                .build();
    }

    public static WebSocketDebateMessage complete(Object report, int totalRounds) {
        return WebSocketDebateMessage.builder()
                .type("COMPLETE")
                .totalRounds(totalRounds)
                .isRunning(false)
                .report(report)
                .build();
    }

    public static WebSocketDebateMessage error(String errorMsg) {
        return WebSocketDebateMessage.builder()
                .type("ERROR")
                .isRunning(false)
                .error(errorMsg)
                .build();
    }
}