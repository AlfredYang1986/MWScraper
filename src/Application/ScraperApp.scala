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
	WebsiteProxyConfigRender("src/Config/Config.xml") map (_.apply)
}