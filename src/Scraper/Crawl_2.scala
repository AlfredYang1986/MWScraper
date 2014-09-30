package Scraper

import Handler.PageHandler_2

trait Crawl_2 {
	def apply(handler :PageHandler_2)
	def url : String
	def name : String
}