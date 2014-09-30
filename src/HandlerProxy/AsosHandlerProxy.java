package HandlerProxy;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


//import jdk.nashorn.internal.objects.NativeArray;
import Model.Item;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.*;
/**
 * 
 * @author FENG
 * 
 */
public class AsosHandlerProxy implements PageHandlerProxy {
	/**
	 * get item information from given URL
	 * each item will have the following details:
	 * Title, Brand, Price, categoryID, Gender, Size, Color
	 * @param URL
	 * @return Item
	 */
	@Override
	public Item parseItem(String url) {
		//url = "http://www.asos.com/au/ASOS/ASOS-Tunic-Jumper-with-Splice-Print/Prod/pgeproduct.aspx?iid=3556638&cid=2637&sh=0&pge=0&pgesize=-1&sort=-1&clr=Navy%2fwhite&gender=women";
		try {
			String title, brand, price, prodCode, category, color, prodDesc, gender, img, discount;
			Document itemPage = Jsoup.connect(url).timeout(0).get();
			if (itemPage != null) {
				Elements itemTitles = itemPage
						.getElementsByClass("product_title");
				if (itemTitles.size() != 0)
					title = itemTitles.get(0).text();
				else
					title = null;
				Elements priceElement = itemPage
						.getElementsByClass("product_price");
				if (priceElement.size() != 0)
					price = priceElement.get(0).child(0).text().replaceAll("[^\\d.$]","");
				else
					price = null;
				if (priceElement.get(1).text()!=null)
					discount = priceElement.get(0).child(1).text().replaceAll("[^\\d.$]","");
				else
					discount = null;
				Elements descElement = itemPage
						.getElementsByClass("product-description");
				if (descElement.size() != 0)
					prodDesc = descElement.get(0).text();
				else
					prodDesc = null;
				Element imgElement = itemPage
						.getElementById("ctl00_ContentMainPage_imgMainImage");
				if (imgElement.hasAttr("src"))
					img = imgElement.attr("src");
				else
					img = null;

				// Find brand location
				brand = url.substring(url.indexOf("www.asos.com/au/")
						+ "www.asos.com/au/".length());
				brand = brand.substring(0, brand.indexOf("/"));
				prodCode = url.substring(url.indexOf("iid="));
				prodCode = prodCode.substring(prodCode.indexOf("=") + 1,
						prodCode.indexOf("&"));
				//cid = url.substring(url.indexOf("cid="));
				//cid = cid.substring(cid.indexOf("=") + 1, cid.indexOf("&"));
				color = url.substring(url.indexOf("clr="));
				color = color.substring(color.indexOf("=") + 1, color.indexOf("&"));

				// get gender
//				gender = url.substring(url.indexOf("gender="));
//				gender = gender.substring(gender.indexOf("=") + 1,
//						gender.length());
				gender = "women";
				
				category = itemPage.select("div.breadcrumbs a[rel=nofollow]").last().text();
				
				/*
				 * convert size to Json object
				 */
//				JsonParser jparser = new JsonParser();
//				JsonArray sizeArray = new JsonArray();
//				JsonObject sizeObject = new JsonObject();
//				Elements sc = itemPage.getElementsByTag("script");
//				for(Element s : sc){
//					String script = s.toString();
//					if(script.indexOf("arrSzeCol_ctl00_ContentMainPage_ctlSeparateProduct") > -1){
//						script = s.childNode(0).toString();
//						script = script.substring(script.indexOf("-->") + 4, script.length());
//						ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
//						engine.eval(script);
//						Object obj = engine.get("arrSzeCol_ctl00_ContentMainPage_ctlSeparateProduct");
//						//json object required, native array here						
//						Gson gson = new Gson();
//						String str = gson.toJson(obj);
//						JsonElement je = jparser.parse(str);
//						JsonObject jo = je.getAsJsonObject();
//						JsonArray ja = new JsonArray();
//						for(Iterator<Entry<String, JsonElement>> iter = jo.entrySet().iterator(); iter.hasNext();){							
//						       ja.add(iter.next().getValue());
//						}
//						for(int i=0;i<ja.size();i++){
//							jo = ja.get(i).getAsJsonObject();
//							sizeObject.add("size", jo.get("1"));
//							sizeObject.add("avaliable", jo.get("3"));
//							sizeArray.add(jparser.parse(sizeObject.toString()));
//						}
//						break;
//					}
//				}

				//FileUtils.writeStringToFile(new File("page.txt"), itemPage.toString());
				System.err.println(url);
				Item item = new Item();
				item.setItemURL(url);
				item.setTitle(title);
				item.setBrand(brand);
				item.setCategory(category);
				item.setColour(color);
				item.setDiscount(discount);
				item.setGender(gender);
				item.setProdCode(prodCode);
				item.setPrice(price);
				
//				item.setSizeArray(sizeArray);
				//Picture Json Object
				JsonObject picObject = new JsonObject();
				// Hard code
				picObject.addProperty("url", img);
				picObject.addProperty("width", 290);
				picObject.addProperty("height", 370);
				item.setPicJsonObject(picObject);

				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
