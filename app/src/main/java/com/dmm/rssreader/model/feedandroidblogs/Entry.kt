package com.dmm.rssreader.model.feedandroidblogs

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "entry", strict = false)
data class Entry(
	@field:Element(name = "title")
	@param:Element(name = "title")
	var title: String? = null,
	@field:Element(name = "published")
	@param:Element(name = "published")
	var published: String? = null,
	@field:Element(name = "content")
	@param:Element(name = "content")
	var content: String? = null,
)
