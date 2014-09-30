package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class DavidJonesScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("DavidJones Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("DavidJones Scraper Prase End...")
	}
	
	def url = p
	def name = "DavidJones"
	  
	def categoryQueryString = "div.directory-block > ul > li > a"
	def itemQueryString = "ul.product-listing > li > a"
	def itmesPerPage(html : Document) : Int = 0
											
	def totalItemsInPage(html : Document) : Int = html.select("div.product-navigation > p")
												.first.text.split(" ").last.toInt

	def cateUrlFromNode : Element => String = _.attr("href")
	def itemUrlFromPage: Element => String = _.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = {
	  	val a = html.select("ul.pagination > li.next > a")
	  	if (a == null) null
	  	else a.attr("href")
	}
	
	def enumLoop(html : Document, page : String, PrintFunc : (Int, Int) => Unit) : List[String] = {
		val pgeSize = itmesPerPage(html)
		val totalItemInPage = totalItemsInPage(html)
		totalPrintFunc(totalItemInPage)
	
		var reVal : List[String] = enumItemInCategory(page) 
		var next = urlForNextPage(html, page, 0)
		while (next != null && next != "") {
			reVal = reVal ::: enumItemInCategory(next)
			PrintFunc(reVal.size, totalItemInPage)
			next = urlForNextPage(html, next, 0)
		}
		
		reVal
	}
}