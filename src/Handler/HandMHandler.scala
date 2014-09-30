package Handler

import HandlerProxy.HandMHandlerProxy
import Application.JSONFileWriter

class HandMHandler extends PageHandler_2 {
	def apply(url : String, webName : String) = {
		val item = (new HandMHandlerProxy).parseItem(url)
		println(item.toJson)
		JSONFileWriter(item, webName)
	}
}