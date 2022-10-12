package com.dmm.rssreader.utils

import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_1
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_2
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_3
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_4
import com.dmm.rssreader.utils.Constants.DATE_PATTERN_OUTPUT
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat

class FeedParser() {

	private val pullParserFactory = XmlPullParserFactory.newInstance()
	private val parser = pullParserFactory.newPullParser()

	fun parse(xml: String, source: String): List<FeedUI> {
		val feedsUI: MutableList<FeedUI> = mutableListOf()
		var feedTitle = ""

		parser.setInput(xml.byteInputStream(), null)

		while (parser.eventType != XmlPullParser.END_DOCUMENT) {
			if(parser.eventType == XmlPullParser.START_TAG && parser.name == "title") {
				feedTitle = readText(parser)
			} else if(parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
				feedsUI.add(readFeedItem(parser, source))
			} else if(parser.eventType == XmlPullParser.START_TAG && parser.name == "entry") {
				feedsUI.add(readFeedItem(parser, source))
			}
			parser.next()
		}

		return feedsUI
	}

	private fun readFeedItem(parse: XmlPullParser, source: String): FeedUI {
		var title = ""
		var feedSource = source
		var description = ""
		var link = ""
		var image = ""
		var published = ""

		while (parse.next() != XmlPullParser.END_TAG) {
			if(parse.eventType == XmlPullParser.START_TAG && parser.name == "title") {
				title = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "link") {
				link = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "description") {
				description = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "content:encoded") {
				description = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "content") {
				description = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "cover_image") {
				image = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "published") {
				published = parseDate(readText(parser))
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "pubDate") {
				published = parseDate(readText(parser))
			} else if(parser.eventType == XmlPullParser.START_TAG) {
				skipTag(parser)
			}
		}

		if(image.isEmpty() ) {
			image = getImageFromContent(description)
		}

		return FeedUI(
			title,
			feedSource,
			description,
			link,
			image,
			published
		)
	}

	fun parseDate(dateString: String): String {
		var result = ""
		val dateFormats: List<String> = listOf(DATE_PATTERN_1, DATE_PATTERN_2, DATE_PATTERN_3,DATE_PATTERN_4)
		run lit@{
			dateFormats.forEach { format ->
				val dateformatted = formattedDate(dateString,format)
				if(!dateformatted.isEmpty()) {
					result = dateformatted
					return@lit
				}
			}
		}


		return result
	}

	fun formattedDate(dateString: String?, dateFormat: String): String {
		try {
			val sdf = SimpleDateFormat(dateFormat).parse(dateString)
			return SimpleDateFormat(DATE_PATTERN_OUTPUT).format(sdf)
		} catch (e: Exception) {
			return ""
		}
		return ""
	}

	private fun readText(parser: XmlPullParser) : String {
		var text = ""
		while (parser.next() != XmlPullParser.END_TAG) {
			if(parser.eventType == XmlPullParser.TEXT) {
				text = parser.text
			}
		}
		return text
	}

	private fun getImageFromContent(content: String?): String {
		var image = ""
		val formats = listOf(
			Constants.FORMAT_JPEG,
			Constants.FORMAT_JPG,
			Constants.FORMAT_PNG,
			Constants.FORMAT_GIF,
			Constants.FORMAT_TIF,
			Constants.FORMAT_BMP
		)
		val formatsPosition = mutableMapOf<String, Int>()
		if (content != null) {
			formats.forEach { it ->
				val position = content.indexOf(it)
				if (position > -1) {
					formatsPosition[it] = position
				}
			}
			if (!formatsPosition.isEmpty()) {
				val endPosition = formatsPosition.minOf { it -> it.value }
				val keyEndPosition = formatsPosition.filterValues { it -> it == endPosition }
				val startPosition = content.indexOf("https")
				keyEndPosition.forEach { (key, value) ->
					image = content.substring(startPosition, value + key.length)
				}
			}
		}
		return image
	}

	@Suppress("ControlFlowWithEmptyBody")
	private fun skipTag(parser: XmlPullParser)  {
		while (parser.next() != XmlPullParser.END_TAG) {
		}
	}
}