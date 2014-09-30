package Application

import Components.WebsiteProxy
import scala.xml.XML
import Handler._
import Scraper._

object WebsiteProxyConfigRender {
	def apply(path : String) : Seq[WebsiteProxy] = {
		def name2Crawl(str : String, url : String) : Crawl_2 = { 
			str match {
			  case "AsosScraper" => new AsosScraper(url)
			  case "DavidJonesScraper" => new DavidJonesScraper(url)
			  case "HandMScraper" => new HandMScraper(url)
			  case _ => null
			}
		}
		
		def name2Handler(str : String) : PageHandler_2 = { 
			str match {
			  case "AsosHandler" => new AsosHandler
			  case "DavidJonesHandler" => new DavidJonesHandler
			  case "HandMHandler" => new HandMHandler
			  case _ => null
			}
		}
	  
		try {
			(XML.loadFile(path) \\ "website") map { nd =>
				new WebsiteProxy((nd \ "@name") text, 
				    (nd \ "@url") text, name2Handler((nd \ "@handler") text), 
				    name2Crawl((nd \ "@crawl") text, (nd \ "@url") text))
			}
		} catch {
		  	case _ => ScraperApp.printer.writeLine("file not exist or something wrong with prasing")
		  	return null
		}
	}
}