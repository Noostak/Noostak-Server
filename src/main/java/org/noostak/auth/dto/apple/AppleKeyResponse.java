package org.noostak.auth.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppleKeyResponse {
    @JsonProperty("keys") List<RsaKey> keys;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RsaKey{
        @JsonProperty("kty") String keyType;
        @JsonProperty("kid") String keyId;
        @JsonProperty("use") String use;
        @JsonProperty("alg") String algorithm;
        @JsonProperty("n") String modulus;
        @JsonProperty("e") String exponent;
    }
}



