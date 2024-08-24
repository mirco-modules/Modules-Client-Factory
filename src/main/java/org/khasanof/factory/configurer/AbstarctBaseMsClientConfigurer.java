package org.khasanof.factory.configurer;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.*;

/**
 * Abstract base class for configuring Feign clients with Spring's encoding and decoding capabilities.
 *
 * <p>Classes extending this abstract base class need to implement the {@link BaseMsClientConfigurer}
 * interface and can customize or override the encoder and decoder behavior as needed.</p>
 *
 * @author Nurislom
 * @see org.khasanof.factory.configurer
 * @since 8/24/2024 8:14 PM
 */
public abstract class AbstarctBaseMsClientConfigurer implements BaseMsClientConfigurer {

    private final FeignEncoderProperties encoderProperties;
    private final ObjectProvider<AbstractFormWriter> formWriterProvider;
    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;

    public AbstarctBaseMsClientConfigurer(FeignEncoderProperties encoderProperties,
                                          ObjectProvider<AbstractFormWriter> formWriterProvider,
                                          ObjectFactory<HttpMessageConverters> messageConverters,
                                          ObjectProvider<HttpMessageConverterCustomizer> customizers) {

        this.encoderProperties = encoderProperties;
        this.formWriterProvider = formWriterProvider;
        this.messageConverters = messageConverters;
        this.customizers = customizers;
    }

    /**
     * Returns a Feign {@link Encoder} configured with Spring's message converters and custom form writers.
     *
     * <p>This method creates an encoder using Spring's {@link SpringEncoder}, which is configured with
     * the provided {@link AbstractFormWriter}, {@link HttpMessageConverters}, and {@link FeignEncoderProperties}.
     * If no custom form writer is available, a default {@link SpringFormEncoder} is used.</p>
     *
     * @return a configured Feign {@link Encoder}.
     */
    @Override
    public Encoder getEncoder() {
        return this.springEncoder(formWriterProvider, this.encoderProperties, customizers);
    }

    /**
     * Returns a Feign {@link Decoder} configured with Spring's message converters.
     *
     * <p>This method creates a decoder using Spring's {@link SpringDecoder} wrapped with
     * an {@link OptionalDecoder} and a {@link ResponseEntityDecoder}, enabling support
     * for optional response entities.</p>
     *
     * @return a configured Feign {@link Decoder}.
     */
    @Override
    public Decoder getDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters, customizers)));
    }

    /**
     * Creates a Feign {@link Encoder} with support for multipart form data and other content types.
     *
     * <p>This method creates a {@link SpringEncoder} configured with the provided {@link AbstractFormWriter}
     * if available, otherwise it defaults to a {@link SpringFormEncoder}. The encoder is also configured with
     * Spring's {@link HttpMessageConverters}, {@link FeignEncoderProperties}, and customizers.</p>
     *
     * @param formWriterProvider the provider for custom form writers.
     * @param encoderProperties the properties for configuring the Feign encoder.
     * @param customizers the customizers for HTTP message converters.
     * @return a configured Feign {@link Encoder}.
     */
    private Encoder springEncoder(ObjectProvider<AbstractFormWriter> formWriterProvider, FeignEncoderProperties encoderProperties, ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        AbstractFormWriter formWriter = formWriterProvider.getIfAvailable();
        return formWriter != null ? new SpringEncoder(new SpringPojoFormEncoder(formWriter), this.messageConverters, encoderProperties, customizers) : new SpringEncoder(new SpringFormEncoder(), this.messageConverters, encoderProperties, customizers);
    }

    /**
     * Custom extension of the {@link SpringFormEncoder} that integrates a custom form writer.
     *
     * <p>This class extends the default {@link SpringFormEncoder} to allow for the addition
     * of a custom {@link AbstractFormWriter} into the multipart content processor, enabling
     * support for custom multipart form data encoding.</p>
     */
    private static class SpringPojoFormEncoder extends SpringFormEncoder {
        SpringPojoFormEncoder(AbstractFormWriter formWriter) {
            MultipartFormContentProcessor processor = (MultipartFormContentProcessor)this.getContentProcessor(ContentType.MULTIPART);
            processor.addFirstWriter(formWriter);
        }
    }
}
