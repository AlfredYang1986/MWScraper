package Scraper

import Handler.PageHandler_2
import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import scala.collection.JavaConverters._
import Application.ScraperApp

trait CategoryCrawl extends Crawl_2 {
	/**
	 * 0. distinct men and women
	 */
	// def womenUrl = url + "/" + "Women"
	// def menUrl = url + "/" + "Men"
		
	/**
	 * 1. search nav to get the url or the category
	 */
	def categoryQueryString : String
	def getCategoryFromNav : List[String] = 
		Jsoup.connect(url).get.select(categoryQueryString)
			.asScala.toList map { iter => iter.attr("href")}
		
	/**
	 * 2. get current page items
	 */
	def itemQueryString : String
	def itmesPerPage(html : Document) : Int
	def totalItemsInPage(html : Document) : Int

	def enumPagesInCategory(categories : List[String]) : List[String] = {
		def enumItemInCategory(cate : String) : List[String] =
			Jsoup.connect(cate).timeout(0).get.select(itemQueryString)
				.asScala.toList map { iter => url + iter.attr("href") }
		
		def enumPages(page : String) : List[String] = {
			val html = Jsoup.connect(page).timeout(0).get
			val pgeSize = itmesPerPage(html)
							 
			val totalItemInPage = totalItemsInPage(html)
					
			ScraperApp.printer.writeLine("there are " + totalItemInPage + " items in this category")
					
			var reVal : List[String] = Nil
			for (index <- 0 to totalItemInPage / pgeSize) { 
				reVal = reVal ::: enumItemInCategory(page + "#pge=" + index)
				ScraperApp.printer.writeLine("now processing " + reVal.size + " of " + totalItemInPage + " items")
			}
			reVal
		}
		
		ScraperApp.printer.writeLine("there are " + categories.size + " categories")
		var reVal : List[String] = Nil
		categories map { iter => 
			reVal = reVal ::: enumPages(iter)
		}
		reVal
	}
		
	/**
	 * 3. handler url to item
	 */
	def enumItems(itemUrls : List[String], handler : PageHandler_2) = itemUrls.distinct map { iter => handler.apply(iter, name) }
}