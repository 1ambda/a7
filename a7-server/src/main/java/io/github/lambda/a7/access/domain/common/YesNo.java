package io.github.lambda.a7.access.domain.common;

public enum YesNo {
    Y("Y"),
    N("N");

    private String value;

    YesNo(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
