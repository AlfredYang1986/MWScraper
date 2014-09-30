package HandlerProxy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Model.Item;

/**
 * @author FENG
 * 
 */
public class HandMHandlerProxy implements PageHandlerProxy {
	final private String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see handler.PageHandler#parseItem(java.lang.String)
	 */
	@Override
	public Item parseItem(String url) {
		Item item = new Item();
		String category, title, brand, price, prodCode, color, prodDesc, gender = null, img, discount = null, size;
		System.err.println(url);
		try{
			Document itemPage = Jsoup.connect(url).userAgent(this.USERAGENT).timeout(0).get();
			if(itemPage != null){
//				category = itemPage.select("ul.breadcrumbs > li:has(a) > a").last().text();
//				title = itemPage.select("ul.breadcrumbs > li").last().text();
				prodCode = url.substring(url.lastIndexOf("=") + 1, url.length());
				if(itemPage.select("ul.breadcrumbs > li").text().contains("LADIES") || itemPage.select("ul.breadcrumbs > li").text().contains("MEN"))
				{	gender = itemPage.select("ul.breadcrumbs > li > a").get(1).text();
					if(gender.equalsIgnoreCase("LADIES"))
						gender = "WOMEN";
				}
				color = itemPage.select("ul#options-articles > li.act").text();//Cannot get some of the color, because that items are no color description on 06/04/2014
				price = itemPage.select("span#text-price").text();
				img = "http:" + itemPage.select("img#product-image").attr("src");
				size = itemPage.select("span#text-selected-variant").text();
				if(size.isEmpty()){
					size = "One Size";
				}
				JsonParser parser = new JsonParser();
				JsonArray sizeArray = new JsonArray();
				JsonObject sizeObject = new JsonObject();
				sizeObject.add("size", parser.parse("\""+size+"\""));
				sizeObject.add("available",parser.parse("true"));
				sizeArray.add(parser.parse(sizeObject.toString()));
				
				//System.out.println();
				item.setItemURL(url);
//				item.setCategory(category);
				item.setCategory("some thing wrong");
//				item.setTitle(title);
				item.setTitle("some thing wrong");
				item.setProdCode(prodCode);
				item.setBrand("H&M");
				item.setGender(gender);
				item.setColour(color);
				item.setPrice(price);
				item.setDiscount(discount);
				item.setPic(img);
				item.setSizeArray(sizeArray);
				//item.setSizeObject(sizeObject);
				//Picture Json Object
				JsonObject picObject = new JsonObject();
				//hard coded
				picObject.addProperty("url", img);
				picObject.addProperty("width", 384);
				picObject.addProperty("height", 449);
				item.setPicJsonObject(picObject);
				return item;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
