package org.khasanof.factory;

import feign.Client;
import feign.Feign;
import org.khasanof.factory.configurer.BaseMsClientConfigurer;
import org.khasanof.modules.client.core.oauth2.AuthorizationHeaderUtil;
import org.khasanof.modules.client.core.oauth2.TokenRelayRequestInterceptor;

/**
 * @author Nurislom
 * @see org.khasanof.factory
 * @since 8/24/2024 7:16 PM
 */
public class SessionBaseMsClientFactory extends AbstractMsClientFactory {

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    public SessionBaseMsClientFactory(Client client, BaseMsClientConfigurer baseMsClientConfigurer, AuthorizationHeaderUtil authorizationHeaderUtil) {
        super(client, baseMsClientConfigurer);
        this.authorizationHeaderUtil = authorizationHeaderUtil;
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
     * {@link TokenRelayRequestInterceptor} for handling authorization, using the provided
     * {@link AuthorizationHeaderUtil}.</p>
     *
     * @param clientClass the class of the Feign client for which the builder is configured.
     * @return a configured {@link Feign.Builder} for the specified client class.
     */
    @Override
    protected Feign.Builder feignBuilder(Class<?> clientClass) {
        return super.feignBuilder(clientClass)
                .requestInterceptor(new TokenRelayRequestInterceptor(authorizationHeaderUtil));
    }
}
