package HandlerProxy;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Model.Item;

/**
 * @author FENG
 *
 */
public class SabaHandlerProxy implements PageHandlerProxy {
	private Set<String> colorList = new HashSet<String>();
	final private String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
	
	@Override
	public Item parseItem(String url) {
		//url = "http://www.saba.com.au/on/demandware.store/Sites-SABA-Site/default/Product-Variation?pid=WW14157&dwvar_WW14157_color=SNOW&dwvar_WW14157_size=L";
		Item item = new Item();
		String category, title, brand, price, prodCode, color, prodDesc, gender = null, img, discount = null, size;
		System.err.println(url);
		try{
			Document itemPage = Jsoup.connect(url).userAgent(this.USERAGENT).timeout(0).get();
			if(itemPage != null){
				category = itemPage.select("ul.breadcrumb li:nth-child(4) > a").text();
				gender = itemPage.select("ul.breadcrumb li:nth-child(3) > a").text();
				if(category.equalsIgnoreCase("WOMENS") || category.equalsIgnoreCase("MENS")){
					String tmp = category;
					category = gender;
					gender = tmp;
				}
				title = itemPage.select("h1.product-name").text();
				prodCode = itemPage.select("span[itemprop=productID]").text();
				img = itemPage.select("img.primary-image").first().attr("src");
				img = img.substring(0, img.indexOf("?"));
				//price = itemPage.select();
				Elements priceElement = itemPage.select("div#product-content div.product-price");
				//System.out.println(priceElement.size()+"\n"+priceElement.first().children().size());
				if(priceElement.first().children().size() < 2){
					price = priceElement.text();
					discount = null;
				}
				else {
					price = priceElement.select("span.price-standard").text().replaceAll("[^\\d.$]","");
					discount = priceElement.select("span.price-sales").text().replaceAll("[^\\d.$]","");
					if(discount.hashCode()==0)
						discount = priceElement.select("span.price-vips").text().replaceAll("[^\\d.$]","");
				}
				color = itemPage.select("span.label-selected-color").text();
				if(!url.contains("/on/")){
					for(Element c : itemPage.select("ul[class=swatches Color] > li.emptyswatch > a[href]")){
						colorList.add(c.attr("href"));
					}
				}
				//size here
				JsonParser parser = new JsonParser();
				JsonElement je = null;
				JsonArray sizeArray = new JsonArray();
				JsonObject sizeObject = new JsonObject();
				Elements sizeList = itemPage.select("ul[class=swatches size] > li");
				for(Element itemSize : sizeList){
					je = parser.parse("\""+itemSize.text().toString()+"\"");
					sizeObject.add("size", je);
					if(itemSize.className().contains("unselectable")){
						sizeObject.add("available", parser.parse("false"));
					}
					else{
						sizeObject.add("available", parser.parse("true"));
					}
					je = parser.parse(sizeObject.toString());
					sizeArray.add(je);
				}
				
				item.setItemURL(url);
				item.setCategory(category);
				item.setTitle(title);
				item.setProdCode(prodCode);
				item.setBrand("SABA");
				item.setGender(gender);
				item.setColour(color);
				item.setPrice(price);
				item.setDiscount(discount);
				//item.setPic(img);
				item.setSizeArray(sizeArray);
				//Picture Json Object
				JsonObject picObject = new JsonObject();
				//hard coded
				picObject.addProperty("url", img);
				picObject.addProperty("width", 394);
				picObject.addProperty("height", 460);
				item.setPicJsonObject(picObject);
				return item;
				
				//System.out.println(sizeObject);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
