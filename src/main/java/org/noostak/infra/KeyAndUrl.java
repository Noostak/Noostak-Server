package org.noostak.infra;


import lombok.Getter;

@Getter
public class KeyAndUrl {

    private final String key;
    private final String url;

    private KeyAndUrl(String givenKey, String givenUrl) {
        this.key = givenKey;
        this.url = givenUrl;
    }

    public static KeyAndUrl of(String key, String url) {
        return new KeyAndUrl(key, url);
    }
}