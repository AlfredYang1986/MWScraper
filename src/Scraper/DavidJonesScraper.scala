package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document

class DavidJonesScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("Asos Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("Asos Scraper Prase End...")
	}
	
	def url = p
	def name = "DavidJones"
	  
	def categoryQueryString = "ul.items > li > a"
	def itemQueryString = "ul#items > li > a"
	def itmesPerPage(html : Document) : Int = html.select("li.page-skip > a")
												.first.attr("href").split("&")
												.find(p => p.startsWith("pgesize"))
												.get.split("=").last.toInt
	def totalItemsInPage(html : Document) : Int = html.select("span.total-items").first.text.toInt
}