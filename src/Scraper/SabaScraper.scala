package Scraper

import Handler.PageHandler_2
import Application.ScraperApp
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class SabaScraper(p : String) extends CategoryCrawl {
	def apply(handler :PageHandler_2) = {
		ScraperApp.printer.writeLine("Saba Scraper Prase Start ...")
		enumItems(enumPagesInCategory(getCategoryFromNav), handler)
		ScraperApp.printer.writeLine("Saba Scraper Prase End...")
	}
	
	def url = p
	def name = "Saba"
	  
	def categoryQueryString = "div.menu-wrapper > div > div > div > a"
	def itemQueryString = "#search-result-items > ul > li > div > div > a"
	def itmesPerPage(html : Document) : Int = { 
		val tmp = html.select("div.select2-result-label").first
		if (tmp == null) 36
		else tmp.text.trim.toInt
	}
	
	def totalItemsInPage(html : Document) : Int = {
		val tmp = html.select("div.select2-result-label").last
		if (tmp == null) 36
		else tmp.text.split("(").last.split(")").head.toInt 
	}
	
	def cateUrlFromNode : Element => String = _.attr("href")
	def itemUrlFromPage: Element => String = _.attr("href")

	def urlForNextPage(html : Document, baseUrl : String, pgeIndex : Int) : String = 
	  	if (baseUrl.contains("?")) baseUrl + "&start=" + pgeIndex *  itmesPerPage(html)
	  	else baseUrl
	  	
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