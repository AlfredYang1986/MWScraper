package Scraper

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import Handler.PageHandler_2
import Application.ScraperApp

class HandMScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("H&M Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("H&M Scraper Prase End...")
	}
	
	def url = p
	def name = "H&M"
	  
	override def cateUrlFilter : Element => Boolean = _.text.trim.toLowerCase == "view all"
	  
	def categoryQueryString = "ul#nav > li > div > ul > li > ul > li > a"
	def itemQueryString = "ul#list-products > li > div > a"
	def itmesPerPage(html : Document) : Int = 24 // hard-code just for now
	
	def totalItemsInPage(html : Document) : Int = html.select("div.bottom-bar > span")
													.first.text.split(":").last.trim.substring(1).toInt
	
	def cateUrlFromNode : Element => String = _.attr("href")
	def itemUrlFromPage: Element => String = _.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = baseUrl + "#pge=" + pgeIndex
	
		def enumLoop(html : Document, page : String, PrintFunc : (Int, Int) => Unit) : List[String] = {
		var reVal : List[String] = Nil
		val pgeSize = itmesPerPage(html)
		println(html.select("div.bottom-bar > span").first.text)
		val totalItemInPage = totalItemsInPage(html)
		totalPrintFunc(totalItemInPage)
	
		for (index <- 0 to totalItemInPage / pgeSize) {
			val next = urlForNextPage(html, page, index)
			if (next != null) reVal = reVal ::: enumItemInCategory(next)
			ScraperApp.printer.writeLine("now processing " + reVal.size + " of " + totalItemInPage + " items")
		}
		reVal
	}
}