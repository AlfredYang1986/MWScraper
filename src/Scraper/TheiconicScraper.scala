package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TheiconicScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("Iconic Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("Iconic Scraper Prase End...")
	}
	
	def url = p
	def name = "Iconic"
	  
	def categoryQueryString = "div.navigation_menu > div > ul > li > a"
	def itemQueryString = "div.product > a"
	def itmesPerPage(html : Document) : Int = 96

	def totalItemsInPage(html : Document) : Int = html.select("div.items-count-wrapper > span")
													.first.text.split(" ").head.toInt
	
	def cateUrlFromNode : Element => String = url + _.attr("href")
	def itemUrlFromPage: Element => String = url + _.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = baseUrl + "?page=" + pgeIndex + 1
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