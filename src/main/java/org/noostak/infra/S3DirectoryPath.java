package org.noostak.infra;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3DirectoryPath {

    DEFAULT("images"),
    MEMBER("member"),
    GROUP("group"),

    ;

    private final String POSTFIX = "/";

    private final String name;

    public String getPath() {
        return name + POSTFIX;
    }

    public String getName() {
        return name;
    }
}