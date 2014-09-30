package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class AsosScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("Asos Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("Asos Scraper Prase End...")
	}
	
	def url = p
	def name = "Asos"
	  
	def categoryQueryString = "ul.items > li > a"
	def itemQueryString = "ul#items > li > a"
	def itmesPerPage(html : Document) : Int = html.select("li.page-skip > a")
												.first.attr("href").split("&")
												.find(p => p.startsWith("pgesize"))
												.get.split("=").last.toInt
	def totalItemsInPage(html : Document) : Int = html.select("span.total-items").first.text.toInt
	
	def cateUrlFromNode : Element => String = iter => iter.attr("href")
	def itemUrlFromPage: Element => String = iter => url + iter.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = baseUrl + "&pge=" + pgeIndex
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