package Handler

import Application.JSONFileWriter
import HandlerProxy.SabaHandlerProxy

class SabaHandler extends PageHandler_2 {
	def apply(url : String, webName : String) = {
		val item = (new SabaHandlerProxy).parseItem(url)
		println(item.toJson)
		JSONFileWriter(item, webName)
	}
}