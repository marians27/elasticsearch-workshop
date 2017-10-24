package pl.marian.training.elasticsearch.springdata.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Arrays.stream;

public enum CountryCode {
    GERMANY("DE"),
    UNITED_KINGDOM("UK");

    private final String code;

    @JsonCreator
    CountryCode(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

    public static CountryCode convertFromCode(String code) {
        return stream(values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

}
