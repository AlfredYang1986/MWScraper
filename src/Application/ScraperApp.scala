package Application

object ScraperApp extends App {
	
	/**
	 * Scraper system log
	 */
	object printer {
		def writeLine(str : String) {
			println(str)
		}
		
		def write(str : String) {
			print(str)
		}
	}

	/**
	 * Scraper system started
	 */
	printer.writeLine("Scraper running ...")
//	println(WebsiteProxyConfigRender("/Users/yangyuan/Desktop/Scala/MWScraper/src/Config/Config.xml"))
	WebsiteProxyConfigRender("/Users/yangyuan/Desktop/Scala/MWScraper/src/Config/Config.xml") map { item => 
		item.apply
	}
}