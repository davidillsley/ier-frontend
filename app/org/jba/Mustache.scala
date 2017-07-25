package org.jba

import com.github.mustachejava.DefaultMustacheFactory
import com.twitter.mustache.ScalaObjectHandler
import java.io.{InputStreamReader, StringWriter}

import org.apache.commons.lang.StringEscapeUtils

import scala.io.Source
import play.api._
import play.api.templates._
import play.api.Configuration._
import play.api.Play.current
import play.twirl.api.Html


trait MustacheAPI {
  def render(template: String, data: Any): Html
}

class JavaMustache extends MustacheAPI{

  private lazy val fs = java.io.File.separator

  private val rootPath = fs + "assets" + fs + "mustache" + fs

  val mf = createMustacheFactory

  private def createMustacheFactory = {
    val factory = new DefaultMustacheFactory {
      // override for load ressouce with play classloader
      override def getReader(resourceName: String): java.io.Reader  = {
        Logger("mustache").debug("read in factory: " + rootPath + resourceName + ".html")
        val input = Play.current.resourceAsStream(rootPath + resourceName  + ".html").getOrElse(throw new Exception("mustache: could not find template: " + resourceName))
        new InputStreamReader(input)
      }
    }
    factory.setObjectHandler(new ScalaObjectHandler)
    factory
  }

  private def readTemplate(template: String) = {
    Logger("mustache").debug("load template: " + rootPath + template)

    val factory = if(Play.isProd) mf else createMustacheFactory

    val input = Play.current.resourceAsStream(rootPath + template + ".html").getOrElse(throw new Exception("mustache: could not find template: " + template))
    val mustache = factory.compile(new InputStreamReader(input), template)

    mustache
  }

  def render(template: String, data: Any): Html = {
    Logger("mustache").debug("Mustache render template " + template)

    val mustache = {
      if(Play.isProd) {
        val maybeTemplate = mf.compile(template)
        if(maybeTemplate == null) {
          readTemplate(template)
        } else maybeTemplate
      } else {
        readTemplate(template)
      }
    }

    val writer = new StringWriter()
    mustache.execute(writer, data).flush()
    writer.close()

    Html(writer.toString())
  }

}

object Mustache {
  lazy val instance = {
    val i = new JavaMustache
    i
  }

  def render(template: String, data: Any): Html = instance.render(template,data)

}