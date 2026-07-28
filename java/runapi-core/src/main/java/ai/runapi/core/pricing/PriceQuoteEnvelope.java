package ai.runapi.core.pricing;
import com.fasterxml.jackson.annotation.JsonProperty;
final class PriceQuoteEnvelope { @JsonProperty("price_quote") private PriceQuote priceQuote; public PriceQuote getPriceQuote() { return priceQuote; } }
