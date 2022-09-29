package com.dmm.rssreader.utils

import android.util.Log
import com.dmm.rssreader.domain.model.FeedUI
import com.dmm.rssreader.utils.Constants.MATCH_SOURCE_BLOGS
import com.dmm.rssreader.utils.Constants.MATCH_SOURCE_MEDIUM
import com.dmm.rssreader.utils.Constants.SOURCE_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.SOURCE_BLOGS
import com.dmm.rssreader.utils.Constants.SOURCE_DANLEW_BLOG
import com.dmm.rssreader.utils.Constants.SOURCE_DEVELOPER_CO
import com.dmm.rssreader.utils.Constants.SOURCE_KOTLIN_WEEKLY
import com.dmm.rssreader.utils.Constants.SOURCE_MEDIUM
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class FeedParser {

	private val pullParserFactory = XmlPullParserFactory.newInstance()
	private val parser = pullParserFactory.newPullParser()

	fun parse(xml: String): List<FeedUI> {
		val feedsUI: MutableList<FeedUI> = mutableListOf()
		var feedTitle = ""

		parser.setInput(xml.byteInputStream(), null)

		try {
			while (parser.eventType != XmlPullParser.END_DOCUMENT) {
				if(parser.eventType == XmlPullParser.START_TAG && parser.name == "title") {
					feedTitle = readText(parser)
				} else if(parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
					feedsUI.add(readFeedItem(parser, feedTitle))
				} else if(parser.eventType == XmlPullParser.START_TAG && parser.name == "entry") {
					feedsUI.add(readFeedItem(parser, feedTitle))
				}
				parser.next()
			}
		}catch (e: Exception) {
			Log.e("EXCEPTION ---> ", "${e.message}")
		}



		return feedsUI
	}

	private fun readFeedItem(parse: XmlPullParser, feedTitle: String): FeedUI {
		var title = ""
		var feedSource = feedTitle
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
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "cover_image") {
				image = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "published") {
				published = readText(parser)
			} else if(parse.eventType == XmlPullParser.START_TAG && parser.name == "pubDate") {
				published = readText(parser)
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

	private fun readText(parser: XmlPullParser) : String {
		var text = ""
		while (parser.next() != XmlPullParser.END_TAG) {
			if(parser.eventType == XmlPullParser.TEXT) {
				text = parser.text
			}
		}
		return text
	}

	private fun determineFeedSource(source: String): String {
		var feedSource = ""
		val sourceMatch = source.uppercase()
		if (sourceMatch.contains(MATCH_SOURCE_BLOGS)) {
			feedSource = SOURCE_ANDROID_BLOGS
		} else if (sourceMatch.contains(MATCH_SOURCE_MEDIUM)) {
			feedSource = SOURCE_MEDIUM
		} else if (sourceMatch.contains(SOURCE_KOTLIN_WEEKLY)) {
			feedSource = SOURCE_KOTLIN_WEEKLY
		} else if (sourceMatch.contains(SOURCE_DANLEW_BLOG)) {
			feedSource = SOURCE_DANLEW_BLOG
		} else {
			feedSource = SOURCE_DEVELOPER_CO
		}
		return feedSource
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