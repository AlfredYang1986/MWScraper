package HandlerProxy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Model.Item;

/**
 * @author FENG
 *
 */
public class UniversalStoreHandlerProxy implements PageHandlerProxy {
	final private String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";

	/* (non-Javadoc)
	 * @see handler.PageHandler#parseItem(java.lang.String)
	 */
	@Override
	public Item parseItem(String url) {
		Item item = new Item();
		String category, title, brand, price = null, prodCode = null, color, prodDesc, gender = null, img, discount = null, size;
		JsonArray sizeArray = new JsonArray();
		System.err.println(url);
		try{
			Document itemPage = Jsoup.connect(url).userAgent(this.USERAGENT).timeout(100000).get();
			JsonParser jparser = new JsonParser();
			Elements sc = itemPage.getElementsByTag("script");
			category = url.substring(url.indexOf("?")+1, url.length());
			title = itemPage.select("div.product-name").text();
			brand = itemPage.select("div.product-name img").attr("title");
			color = itemPage.select("table#product-attribute-specs-table tr:last-child > td").text();
			gender = url.substring(url.indexOf(".com/"));
			gender = gender.substring(gender.indexOf("/")+1,gender.lastIndexOf("/"));
			img = itemPage.select("img#image").attr("src");
			
			for(Element s : sc){
				if(s.toString().indexOf("spConfig") > -1){
					String tmp = s.childNode(0).toString();
					tmp = tmp.substring(tmp.indexOf("{"),tmp.lastIndexOf("}")+1);
					JsonObject jo = jparser.parse(tmp).getAsJsonObject();
					price = "$" + jo.get("basePrice").toString().replace("\"", "");
					discount = "$" + jo.get("oldPrice").toString().replace("\"", "");;
					if(price.equals(discount)){
						discount = null;
					}
					prodCode = jo.get("productId").toString().replace("\"", "");;
					/*String tmp1 = tmp.substring(tmp.indexOf("basePrice"));
					price = "$" + tmp1.substring(tmp1.indexOf(":") + 2,tmp1.indexOf(",") - 1);
					discount = tmp1.substring(tmp1.indexOf("oldPrice"));
					discount = "$" + discount.substring(discount.indexOf(":") + 2,discount.indexOf(",") - 1);
					prodCode = tmp.substring(tmp.indexOf("productId"));
					prodCode = prodCode.substring(prodCode.indexOf(":") + 2,prodCode.indexOf(",") - 1);
					*/
					JsonArray jarray = new JsonArray();
					
					JsonObject sizeObject = new JsonObject();
					//tmp = tmp.substring(tmp.indexOf("["),tmp.lastIndexOf("]")+1);
					//get("133"): unknown 133 here, unsafe
					jarray = jo.get("attributes").getAsJsonObject().get("133").getAsJsonObject().get("options").getAsJsonArray();
					for (int i = 0; i < jarray.size(); i++){
						size = jarray.get(i).getAsJsonObject().get("label").toString();
						sizeObject.add("size", jparser.parse(size));
						sizeObject.add("available", jparser.parse("true"));
						sizeArray.add(jparser.parse(sizeObject.toString()));
					}
					//substring from key name to first "," then convert to json object
					break;
				}
			}
			
			item.setItemURL(url);
			item.setCategory(category);
			item.setTitle(title);
			item.setProdCode(prodCode);
			item.setBrand(brand);
			item.setGender(gender);
			item.setColour(color);
			item.setPrice(price);
			item.setDiscount(discount);
			item.setPic(img);
			item.setSizeArray(sizeArray);
			//Picture Json Object
			JsonObject picObject = new JsonObject();
			//hard coded
			picObject.addProperty("url", img);
			picObject.addProperty("width", 600);
			picObject.addProperty("height", 900);
			item.setPicJsonObject(picObject);
			return item;
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}