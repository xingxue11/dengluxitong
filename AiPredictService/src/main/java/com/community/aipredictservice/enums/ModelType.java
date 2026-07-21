package com.community.aipredictservice.enums;

public enum ModelType {
    KIMI("kimi", "Kimi", "moonshot-v1-8k"),
    DOUBAO("doubao", "豆包", "doubao-3"),
    QIANWEN("qianwen", "通义千问", "qwen-plus"),
    ZHIPU("zhipu", "智谱AI", "glm-4"),
    LOCAL("local", "本地模型", "llama3");

    private final String code;
    private final String name;
    private final String modelName;

    ModelType(String code, String name, String modelName) {
        this.code = code;
        this.name = name;
        this.modelName = modelName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getModelName() {
        return modelName;
    }

    public static ModelType fromCode(String code) {
        for (ModelType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown model code: " + code);
    }
}