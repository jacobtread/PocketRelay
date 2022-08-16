package com.jacobtread.kme.utils

import com.jacobtread.netty.http.HttpRequest
import com.jacobtread.netty.http.HttpResponse
import com.jacobtread.netty.http.response
import com.jacobtread.netty.http.responseText
import com.jacobtread.xml.OutputOptions
import com.jacobtread.xml.XmlVersion
import com.jacobtread.xml.element.XmlRootNode
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

/**
 * responseXml Creates an XML response that initializes the xml node tree
 * using the provided init function which is passed the root node with the
 * provided node name as the receiver
 *
 * Automatically sets the encoding and XML version for the root node so
 * that that information is included in the built xml
 *
 * @param rootName The name of the root node
 * @param init The initializer function
 * @receiver The newly created root node
 * @return The HttpResponse created from the encoded XML
 */
inline fun responseXml(rootName: String, init: XmlRootNode.() -> Unit): HttpResponse {
    val rootNode = XmlRootNode(rootName)
    rootNode.encoding = "UTF-8"
    rootNode.version = XmlVersion.V10
    rootNode.init()
    val outputOptions = OutputOptions(
        prettyPrint = false
    )
    return responseText(rootNode.toString(outputOptions), "text/xml;charset=UTF-8")
}

inline fun responseJson(
    status: HttpResponseStatus = HttpResponseStatus.OK,
    init: JsonObjectBuilder.() -> Unit,
): HttpResponse {
    val jsonObject = buildJsonObject(init)
    val content = Json.encodeToString(jsonObject)
    return response(status, Unpooled.copiedBuffer(content, Charsets.UTF_8), "application/json")
}

inline fun <reified T> responseJson(
    status: HttpResponseStatus = HttpResponseStatus.OK,
    response: T,
): HttpResponse {
    val content = Json.encodeToString(response)
    return response(status, Unpooled.copiedBuffer(content, Charsets.UTF_8), "application/json")
}

inline fun <reified T> HttpRequest.contentJson(): T {
    val content = contentString()
    return Json.decodeFromString(content)
}