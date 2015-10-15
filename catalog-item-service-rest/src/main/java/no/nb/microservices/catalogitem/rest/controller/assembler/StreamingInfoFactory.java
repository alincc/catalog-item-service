package no.nb.microservices.catalogitem.rest.controller.assembler;

public class StreamingInfoFactory {

    public static StreamingInfoStrategy getStreamingInfoStrategy(String mediatype) {
        if ("musikk".equalsIgnoreCase(mediatype) ||
                "film".equalsIgnoreCase(mediatype) ||
                "radio".equalsIgnoreCase(mediatype) ||
                "lydopptak".equalsIgnoreCase(mediatype)) {
            return new StreamableStrategy();
        } else {
            return new NoStreamableStrategy();
        }
    }
}
