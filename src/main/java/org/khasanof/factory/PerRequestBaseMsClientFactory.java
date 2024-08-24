package org.khasanof.factory;

import feign.Client;
import feign.Feign;
import org.khasanof.factory.configurer.BaseMsClientConfigurer;
import org.khasanof.modules.client.core.config.ModulesClientCoreProperties;
import org.khasanof.modules.client.core.oauth2.keycloak.KeycloakInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;

/**
 * @author Nurislom
 * @see org.khasanof.factory
 * @since 8/24/2024 8:05 PM
 */
public class PerRequestBaseMsClientFactory extends AbstractMsClientFactory {

    private final ModulesClientCoreProperties modulesClientCoreProperties;

    public PerRequestBaseMsClientFactory(Client client, BaseMsClientConfigurer baseMsClientConfigurer, ModulesClientCoreProperties modulesClientCoreProperties) {
        super(client, baseMsClientConfigurer);
        this.modulesClientCoreProperties = modulesClientCoreProperties;
    }

    /**
     * Creates an instance of the specified client class using a configured Feign builder.
     *
     * <p>This method configures a {@link Feign.Builder} specific to the provided client class and
     * uses it to create an instance of the client. The client is built using the base URL
     * configuration provided within the Feign setup.</p>
     *
     * @param clientClass the class of the Feign client to create.
     * @param <T> the type of the Feign client.
     * @return an instance of the specified Feign client class.
     */
    @Override
    public <T> T create(Class<T> clientClass) {
        Feign.Builder builder = feignBuilder(clientClass);
        return build(builder, clientClass);
    }

    /**
     * Creates an instance of the specified client class using a configured Feign builder with a custom URL.
     *
     * <p>This method configures a {@link Feign.Builder} specific to the provided client class and
     * uses it to create an instance of the client, using the specified base URL.</p>
     *
     * @param clientClass the class of the Feign client to create.
     * @param url the base URL for the Feign client.
     * @param <T> the type of the Feign client.
     * @return an instance of the specified Feign client class configured with the provided URL.
     */
    @Override
    public <T> T create(Class<T> clientClass, String url) {
        Feign.Builder builder = feignBuilder(clientClass);
        return build(builder, clientClass, url);
    }

    /**
     * Configures a {@link Feign.Builder} with custom settings for the specified client class.
     *
     * <p>This method overrides the base Feign builder configuration to include a custom
     * {@link KeycloakInterceptor} for handling authorization, leveraging a {@link RestTemplateBuilder}
     * and configuration properties.</p>
     *
     * @param clientClass the class of the Feign client for which the builder is configured.
     * @return a configured {@link Feign.Builder} for the specified client class.
     */
    @Override
    protected Feign.Builder feignBuilder(Class<?> clientClass) {
        return super.feignBuilder(clientClass)
                .requestInterceptor(new KeycloakInterceptor(new RestTemplateBuilder(), modulesClientCoreProperties));
    }
}
