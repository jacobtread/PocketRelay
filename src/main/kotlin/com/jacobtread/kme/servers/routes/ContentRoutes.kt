package com.jacobtread.kme.servers.routes

import com.jacobtread.netty.http.responseResource
import com.jacobtread.netty.http.router.RoutingGroup
import com.jacobtread.netty.http.router.group
import com.jacobtread.netty.http.setHeader

/**
 * routeContents Add the routing catching-all for the ME3 assets
 * this is used for sending images for the shop and related
 */
fun RoutingGroup.routeContents() {
    group("content") {
        everything {
            val path = param("*")
            val fileName = path.substringAfterLast('/')
            responseResource(fileName, "public")
//                .setHeader("Accept-Ranges", "bytes")
//                .setHeader("ETag", "524416-1333666807000")
        }
    }
}