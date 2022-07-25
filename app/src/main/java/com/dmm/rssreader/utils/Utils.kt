package com.dmm.rssreader.utils

import android.util.Log
import com.dmm.rssreader.model.Feed
import com.dmm.rssreader.model.FeedUI
import com.dmm.rssreader.model.Item
import com.dmm.rssreader.model.feedandroidblogs.Entry
import com.dmm.rssreader.model.feedandroidblogs.FeedAndroidBlogs
import com.dmm.rssreader.utils.Constants.FORMAT_BMP
import com.dmm.rssreader.utils.Constants.FORMAT_GIF
import com.dmm.rssreader.utils.Constants.FORMAT_JPEG
import com.dmm.rssreader.utils.Constants.FORMAT_JPG
import com.dmm.rssreader.utils.Constants.FORMAT_PNG
import com.dmm.rssreader.utils.Constants.FORMAT_TIF
import com.dmm.rssreader.utils.Constants.MATCH_SOURCE_APPLE
import com.dmm.rssreader.utils.Constants.MATCH_SOURCE_BLOGS
import com.dmm.rssreader.utils.Constants.MATCH_SOURCE_MEDIUM
import com.dmm.rssreader.utils.Constants.SOURCE_APPLE
import com.dmm.rssreader.utils.Constants.SOURCE_BLOGS
import com.dmm.rssreader.utils.Constants.SOURCE_MEDIUM
import kotlin.collections.get as get1

class Utils {

	companion object {
		fun <T> MapResponse(response: T): List<FeedUI> {
			var feedUIList: MutableList<FeedUI> = mutableListOf()
			when(response) {
				is Feed -> {
					response.itemList?.forEach { item ->
						feedUIList.add(fromItemFeedToFeedUI(item, response.title))
					}
				}
				is FeedAndroidBlogs -> {
					response.entry?.forEach { item ->
						feedUIList.add(fromEntryToFeedUI(item, response.title))
					}
				}
			}
			return feedUIList.toList()
		}

		fun fromItemFeedToFeedUI(item: Item, feedSource: String): FeedUI {
			return FeedUI(
				feedSource = determineFeedSource(feedSource),
				title = item.title,
				published = item.pubDate,
				link = item.link,
				description = item.description,
				image = getImageFromContent(item.description)
			)
		}

		fun fromEntryToFeedUI(entry: Entry, feedSource: String): FeedUI {
			return FeedUI(
				feedSource = determineFeedSource(feedSource),
				title = entry.title,
				published = entry.published,
				link = "",
				image = getImageFromContent(entry.content)
			)
		}

		fun determineFeedSource(source: String): String {
			var feedSource = ""
			val sourceMatch = source.lowercase()
			if(sourceMatch.contains(MATCH_SOURCE_BLOGS.lowercase())) {
				feedSource = SOURCE_BLOGS
			} else if(sourceMatch.contains(MATCH_SOURCE_MEDIUM.lowercase())) {
				feedSource = SOURCE_MEDIUM
			} else if (sourceMatch.contains(MATCH_SOURCE_APPLE.lowercase())) {
				feedSource = SOURCE_APPLE
			}
			return feedSource
		}

		fun getImageFromContent(content: String?): String {
			var image = ""
			val formats = listOf(FORMAT_JPEG, FORMAT_JPG, FORMAT_PNG, FORMAT_GIF, FORMAT_TIF, FORMAT_BMP)
			val formatsPosition = mutableMapOf<String, Int>()
			if(content != null) {
				formats.forEach { it ->
					val position = content.indexOf(it)
					if( position > -1) {
						formatsPosition[it] = position
					}
				}
				if(!formatsPosition.isEmpty()) {
					val endPosition = formatsPosition.minOf { it -> it.value }
					val keyEndPosition = formatsPosition.filterValues { it -> it == endPosition }
					val startPosition = content.indexOf("https")
					keyEndPosition.forEach { (key, value) ->
						image = content.substring(startPosition, value + key.length )
					}
				}
			}
			return image
		}
	}


}