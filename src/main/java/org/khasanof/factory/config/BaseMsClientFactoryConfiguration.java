package org.khasanof.factory.config;

import feign.Client;
import org.khasanof.factory.BaseMsClientFactory;
import org.khasanof.factory.PerRequestBaseMsClientFactory;
import org.khasanof.factory.SessionBaseMsClientFactory;
import org.khasanof.factory.configurer.BaseMsClientConfigurer;
import org.khasanof.factory.enumeration.ReceiveTokenStrategy;
import org.khasanof.modules.client.core.config.ModulesClientCoreProperties;
import org.khasanof.modules.client.core.oauth2.AuthorizationHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.factory
 * @since 8/24/2024 8:10 PM
 */
@Configuration
public class BaseMsClientFactoryConfiguration {

    @Autowired
    private Client client;

    @Autowired
    private BaseMsClientConfigurer baseMsClientConfigurer;

    @Autowired
    private ModulesClientCoreProperties modulesClientCoreProperties;

    @Autowired
    private AuthorizationHeaderUtil authorizationHeaderUtil;

    /**
     * Creates and configures a {@link BaseMsClientFactory} bean based on the configured strategy.
     *
     * <p>This method returns an instance of {@link BaseMsClientFactory} configured according to the
     * strategy defined in {@link BaseMsClientConfigurer}. If the strategy is {@link ReceiveTokenStrategy#PER_REQUEST},
     * it returns a {@link PerRequestBaseMsClientFactory} instance. Otherwise, it returns a
     * {@link SessionBaseMsClientFactory} instance.</p>
     *
     * @return a {@link BaseMsClientFactory} bean configured according to the strategy defined in
     *         {@link BaseMsClientConfigurer}.
     */
    @Bean
    public BaseMsClientFactory baseMsClientFactory() {
        if (Objects.equals(baseMsClientConfigurer.getStrategy(), ReceiveTokenStrategy.PER_REQUEST)) {
            return new PerRequestBaseMsClientFactory(client, baseMsClientConfigurer, modulesClientCoreProperties);
        }
        return new SessionBaseMsClientFactory(client, baseMsClientConfigurer, authorizationHeaderUtil);
    }
}
