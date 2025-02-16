package org.noostak.group.config;

import org.noostak.group.application.InvitationCodeGenerator;
import org.noostak.group.domain.DefaultInvitationCodePolicy;
import org.noostak.group.domain.InvitationCodePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class InviteCodeGeneratorConfig {

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public InvitationCodePolicy inviteCodePolicy() {
        return new DefaultInvitationCodePolicy();
    }

    @Bean
    public InvitationCodeGenerator inviteCodeGenerator(SecureRandom random, InvitationCodePolicy policy) {
        return new InvitationCodeGenerator(random, policy);
    }
}
