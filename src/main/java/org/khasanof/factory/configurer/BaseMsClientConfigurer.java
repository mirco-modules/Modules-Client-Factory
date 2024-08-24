package org.khasanof.factory.configurer;

import feign.codec.Decoder;
import feign.codec.Encoder;
import org.khasanof.factory.enumeration.ReceiveTokenStrategy;

/**
 * Interface for configuring microservice clients in a Feign-based environment.
 *
 * <p>This interface defines the methods required for configuring a microservice client,
 * including obtaining the service name, token strategy, and the Feign {@link Encoder} and
 * {@link Decoder} used for request and response handling.</p>
 *
 * @author Nurislom
 * @see org.khasanof.factory.configurer
 * @since 8/24/2024 7:43 PM
 */
public interface BaseMsClientConfigurer {

    /**
     * Returns the name of the microservice associated with this client configuration.
     *
     * <p>The service name is typically used for identifying the target microservice
     * when making requests or logging activities.</p>
     *
     * @return the name of the microservice as a {@link String}.
     */
    String getServiceName();

    /**
     * Returns the token strategy for handling authentication tokens.
     *
     * <p>This method provides the strategy used for acquiring and applying
     * authentication tokens for the microservice client, which can vary
     * depending on whether tokens are received per request or maintained in a session.</p>
     *
     * @return the {@link ReceiveTokenStrategy} used by the client.
     */
    ReceiveTokenStrategy getStrategy();

    /**
     * Returns the Feign {@link Encoder} used for encoding requests.
     *
     * <p>The encoder is responsible for converting request objects into
     * the appropriate format (e.g., JSON, form data) before they are sent
     * to the microservice.</p>
     *
     * @return the Feign {@link Encoder} configured for this client.
     */
    Encoder getEncoder();

    /**
     * Returns the Feign {@link Decoder} used for decoding responses.
     *
     * <p>The decoder is responsible for converting responses from the
     * microservice into the appropriate object format expected by the client.</p>
     *
     * @return the Feign {@link Decoder} configured for this client.
     */
    Decoder getDecoder();
}
