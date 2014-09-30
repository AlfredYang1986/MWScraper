package Handler

import HandlerProxy.AsosHandlerProxy
import Application.JSONFileWriter

class AsosHandler extends PageHandler_2 {
	def apply(url : String, webName : String) = {
		val item = (new AsosHandlerProxy).parseItem(url)
		println(item.toJson)
		JSONFileWriter(item, webName)
	}
}