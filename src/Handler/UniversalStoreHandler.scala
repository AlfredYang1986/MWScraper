package Handler

import HandlerProxy.UniversalStoreHandlerProxy
import Application.JSONFileWriter

class UniversalStoreHandler extends PageHandler_2 {
	def apply(url : String, webName : String) = {
		val item = (new UniversalStoreHandlerProxy).parseItem(url)
		println(item.toJson)
		JSONFileWriter(item, webName)
	}
}