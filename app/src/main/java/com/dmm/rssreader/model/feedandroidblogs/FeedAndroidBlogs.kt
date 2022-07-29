package com.dmm.rssreader.model.feedandroidblogs

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "feed", strict = false)
data class FeedAndroidBlogs(
	@field:Element(name = "title")
	@param:Element(name = "title")
	var title: String = "",
	@field:ElementList(name = "entry", inline = true, required = false)
	@param:ElementList(name = "entry", inline = true, required = false)
	val entry: List<Entry>? = null,
)
