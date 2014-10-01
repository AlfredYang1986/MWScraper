package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class UniversalStoreScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("Universal Store Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("Universal Store Scraper Prase End...")
	}
	
	def url = p
	def name = "Universal Store"
	  
	def categoryQueryString = "ul#nav > li > a"
	def itemQueryString = "ul.products-grid > li > div > a"
	def itmesPerPage(html : Document) : Int = 100
	
	def totalItemsInPage(html : Document) : Int = {
		val tmp = html.select("div.pages > ol > li")
		if (tmp == null) 100
		else tmp.last.child(0).text.trim.toInt * 100
	}
	
	def cateUrlFromNode : Element => String = _.attr("href")
	def itemUrlFromPage: Element => String = _.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = baseUrl + "?p=" + pgeIndex + 1
	def enumLoop(html : Document, page : String, PrintFunc : (Int, Int) => Unit) : List[String] = {
		var reVal : List[String] = Nil
		val pgeSize = itmesPerPage(html)
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