package uk.gov.gds.ier.serialiser

class JsonSerialiser {
  def toJson(obj: AnyRef):String = {
    JacksonWrapper.serialize(obj)
  }

  def fromJson[A](json: String)(implicit m: Manifest[A]): A = {
    JacksonWrapper.deserialize(json)
  }
}

import java.lang.reflect.{ParameterizedType, Type}
import java.text.DateFormat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.`type`.TypeReference;

object JacksonWrapper {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new JodaParseModule)
  mapper.registerModule(new JodaSerializeModule)
  mapper.setDateFormat(DateFormat.getDateTimeInstance)

  def serialize(value: Any): String = {
    import java.io.StringWriter
    val writer = new StringWriter()
    mapper.writeValue(writer, value)
    writer.toString
  }

  def deserialize[T: Manifest](value: String) : T =
    mapper.readValue(value, typeReference[T])

  private [this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private [this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) { m.erasure }
    else new ParameterizedType {
      def getRawType = m.erasure
      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray
      def getOwnerType = null
    }
  }
}
