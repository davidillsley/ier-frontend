package uk.gov.gds.ier.serialiser

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, JsonSerializer, SerializerProvider}
import org.joda.time.DateTime

class JodaSerializeModule extends SimpleModule {
  this.addSerializer(classOf[DateTime], new JodaWithTimeZoneSerializer())

  class JodaWithTimeZoneSerializer extends JsonSerializer[DateTime] {
    def deserialize(parser: JsonParser, context: DeserializationContext): DateTime = {
      val jsonStr = parser.readValueAs(classOf[String])
      DateTime.parse(jsonStr)
    }

    override def serialize(value: DateTime, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
      gen.writeString(value.toString())
    }
  }
}
