package Components

import Handler.PageHandler_2
import Scraper.Crawl_2

case class WebsiteProxy(name : String, baseUrl : String, handler : PageHandler_2, crawl : Crawl_2) {
	override def toString = "Proxy: \n\tname: " + name + "\n\turl" + baseUrl 
	
	def apply = {
		if (crawl != null && handler != null) crawl(handler)
	}
}