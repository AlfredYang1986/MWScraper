package Application

import Model.Item
import java.io.PrintWriter
import java.io.File

object JSONFileWriter {
	def apply(item : Item, name : String) = {
		val writer = new PrintWriter(new File(name))
		writer.write(item.toJson)
		writer.close()
	}
}