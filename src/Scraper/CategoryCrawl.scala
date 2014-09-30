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
	def cateUrlFromNode : Element => String
	def cateUrlFilter : Element => Boolean = _ => true
	
	def getCategoryFromNav : List[String] = 
		Jsoup.connect(url).timeout(0).get.select(categoryQueryString)
//			.asScala.toList filter cateUrlFilter map cateUrlFromNode
//			.asScala.toList map cateUrlFromNode
			.asScala.toList.head :: Nil map cateUrlFromNode // only first for test
		
	/**
	 * 2. get current page items
	 */
	def itemQueryString : String
	def itmesPerPage(html : Document) : Int
	
	def totalItemsInPage(html : Document) : Int
	def totalPrintFunc(t : Int) = 
		ScraperApp.printer.writeLine("there are " + t + " items in this category")
		
	def itemUrlFromPage: Element => String
	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String
	
	def enumLoop(html : Document, page : String, PrintFunc : (Int, Int) => Unit) : List[String]
	def enumLoopPrintFunc(s : Int, t : Int) = 
		ScraperApp.printer.writeLine("now processing " + s + " of " + t + " items")
	
	def enumItemInCategory(cate : String) : List[String] =
		Jsoup.connect(cate).timeout(0).get.select(itemQueryString)
			.asScala.toList map itemUrlFromPage
//			.asScala.toList.head :: Nil map itemUrlFromPage  // only first for test
			
	def enumPagesInCategory(categories : List[String]) : List[String] = {
		def enumPages(page : String) : List[String] = {
			val html = Jsoup.connect(page).timeout(0).get
			enumLoop(html, page, enumLoopPrintFunc)
		}
		
		ScraperApp.printer.writeLine("there are " + categories.size + " categories")
		var reVal : List[String] = Nil
		categories map { iter => 
			reVal = reVal ::: enumPages(iter)
		}
		reVal.distinct
	}
		
	/**
	 * 3. handler url to item
	 */
	def enumItems(itemUrls : List[String], handler : PageHandler_2) = 
		itemUrls map (handler(_, name))
}