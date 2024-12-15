package com.example.chatapp.model.serializer;

import com.example.chatapp.model.BaseDocument;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Jackson serializer used on fields extending {@link BaseDocument}.
 * <p>
 * It writes the id of a given document to JSON.
 */
public class DocumentIdSerializer extends StdSerializer<BaseDocument> {

    public DocumentIdSerializer() {
        super(BaseDocument.class);
    }

    @Override
    public void serialize(BaseDocument baseDocument, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(baseDocument.getId());
    }
}
