package HandlerProxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Model.Item;

/**
 * @author FENG
 *
 */
public class DavidJonesHandlerProxy implements PageHandlerProxy {
	private List<String> colorList = new ArrayList<String>();
	final private String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
	
	@Override
	public Item parseItem(String url) {
		//url = "http://shop.davidjones.com.au/djs/en/davidjones/waterproof-zip-thru-jacket";
		Item item = new Item();
		String category, title, brand, price, prodCode, color, prodDesc, gender, img, discount;
		System.err.println(url);
		try{
			Document itemPage = Jsoup.connect(url).userAgent(this.USERAGENT).timeout(0).get();
			if(itemPage != null){
				category = itemPage.select("ul#breadcrumb > li:has(a)").last().text();
				title = itemPage.select("h1#catalog_link").text();
				prodCode = itemPage.select("input#productId").attr("value");
				brand = itemPage.select("input#manufacturerName").attr("value");
				gender = null;
				//url = itemPage.select("ul#breadcrumb > li").text();
				if(itemPage.select("ul#breadcrumb > li").text().contains("Women") || itemPage.select("ul#breadcrumb > li").text().contains("Men"))
					gender = itemPage.select("ul#breadcrumb > li").get(1).text();
				color = itemPage.select("div#colourVariationJSONData").text();
				//get color
				JsonParser parser = new JsonParser();
				JsonArray colorArray = parser.parse(color).getAsJsonArray();
				for(JsonElement colorElement : colorArray){
					if(colorElement.isJsonObject()){
						Iterator<Entry<String, JsonElement>> colorit = colorElement.getAsJsonObject().entrySet().iterator();
						while(colorit.hasNext()){
							Entry<String, JsonElement> entry = colorit.next();
							if(entry.getKey().equalsIgnoreCase("colour"))
								colorList.add(entry.getValue().toString().replaceAll("\"", ""));
						}
					}
				}
				price = itemPage.select("div.prices > div.right > span.price").text().replaceAll("[^\\d.$]","");
				//cal how many "$" symbol is the string have and split it to two part.
				//first part is old price, second part is discount
				discount = null;
				if(price.length() - price.replaceAll("\\$", "").length() > 1){
					discount = price.substring(price.lastIndexOf("$"),price.length());
					price = price.replace(discount, "");
					
				}
				img = itemPage.select("div.fluid-display img").attr("src");
				
				//size here. it is not easy to get size, always
				String itemSize = itemPage.select("div#entitledItem_"+prodCode).text();
				JsonArray sizeList = parser.parse(itemSize).getAsJsonArray();
				
				JsonArray sizeArray = new JsonArray();
				String size = "One Size";
				JsonElement available = parser.parse("true");
				for(JsonElement sizeElement : sizeList){
					Iterator<Entry<String, JsonElement>> sizeit = sizeElement.getAsJsonObject().entrySet().iterator();
					while(sizeit.hasNext()){
						JsonObject sizeObject = new JsonObject();
						Entry<String, JsonElement> entry = sizeit.next();
						if(entry.getKey().equalsIgnoreCase("Attributes")){
							if(!entry.getValue().isJsonNull()){
								size = entry.getValue().toString();
								String[] values = size.split(",");
								if(values.length>1){
									if(values[0].contains("Size"))
										size = values[0].substring(values[0].indexOf("_") + 1, values[0].indexOf(":")-1);
									else if(values[1].contains("Size"))
										size = values[1].substring(values[1].indexOf("_") + 1, values[1].indexOf(":")-1);
								}
								else{
									size = "One Size";
								}
								sizeObject.add("size", parser.parse("\""+size+"\""));
								sizeObject.add("available", available);
							}
							sizeArray.add(sizeObject);
						}
						//sizeit.remove();
					}
				}
				item.setCategory(category);
				item.setTitle(title);
				item.setProdCode(prodCode);
				item.setBrand(brand);
				item.setGender(gender);
				//item.setColour(color);
				item.setPrice(price);
				item.setDiscount(discount);
				item.setPic(img);
				item.setSizeArray(sizeArray);
				//Picture Json Object
				JsonObject picObject = new JsonObject();
				//hard coded
				picObject.addProperty("url", img);
				picObject.addProperty("width", 330);
				picObject.addProperty("height", 430);
				item.setPicJsonObject(picObject);
				return item;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
