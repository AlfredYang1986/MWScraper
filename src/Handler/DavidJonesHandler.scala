package Handler

import Application.JSONFileWriter
import HandlerProxy.DavidJonesHandlerProxy

class DavidJonesHandler extends PageHandler_2 {
	def apply(url : String, webName : String) = {
		val item = (new DavidJonesHandlerProxy).parseItem(url)
		println(item.toJson)
		JSONFileWriter(item, webName)
	}
}