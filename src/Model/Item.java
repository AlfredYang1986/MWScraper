package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

public class Item {
	private String title, prod_code, brand, category, gender, colour, size,
			size_type, prod_desc, price, discount, pic, material;
	private String itemURL;
	private int picWidth, picHeight, categoryID, brandID; // unit: px
	private int abstractItemID;
	private JsonObject sizeObject;
	private JsonArray sizeArray;
	private JsonObject picObject;
	private List<String> sizeList;
	private List<Boolean> sizeAvalible;
	
	public Item(){}
	
	/**
	 * get item title
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * set item title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get item product code
	 * @return
	 */
	public String getProdCode() {
		return prod_code;
	}
	/**
	 * set item product code
	 * @param prod_code
	 */
	public void setProdCode(String prod_code) {
		this.prod_code = prod_code;
	}
	/**
	 * get item category
	 * @return
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * set item category
	 * @param category_id
	 */
	public void setCategoryId(int category_id) {
		this.categoryID = category_id;
	}
	/**
	 * get item gender
	 * @return
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * set item gender
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * get item color
	 * @return
	 */
	public String getColour() {
		return colour;
	}
	/**
	 * set item color
	 * @param colour
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}
	/**
	 * get item size
	 * @return
	 */
	public String getSize() {
		return size;
	}
	/**
	 * set item size
	 * @param size
	 */
	public void setSize(String size){
		this.size = size;
	}
	/**
	 * get item size type
	 * @return
	 */
	public String getSize_type() {
		return size_type;
	}
	/**
	 * set item size type
	 * @param size_type
	 */
	public void setSizeType(String size_type) {
		this.size_type = size_type;
	}
	/**
	 * get item description
	 * @return
	 */
	public String getProdDesc() {
		return prod_desc;
	}
	/**
	 * set item description
	 * @param prod_desc
	 */
	public void setProdDesc(String prod_desc) {
		this.prod_desc = prod_desc;
	}
	/**
	 * get item price
	 * @return
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * set item price
	 * @param price
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * get item price
	 * @return
	 */
	public String getDiscount() {
		return discount;
	}
	/**
	 * set item discount
	 * @param discount
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	/**
	 * get item brand
	 * @return
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * set item brand
	 * @param brand
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * get item picture
	 * @return
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * set item picture
	 * @param pic
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * get item picture width
	 * @return
	 */
	public int getPicWidth() {
		return picWidth;
	}
	/**
	 * set item picture width
	 * @param picWidth
	 */
	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}
	/**
	 * get item picture height
	 * @return
	 */
	public int getPicHeight() {
		return picHeight;
	}
	/**
	 * set item picture height
	 * @param picHeight
	 */
	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}
	/**
	 * set item size JSON object
	 * @param sizeObject
	 */
	public void setSizeObject(JsonObject sizeObject) {
		this.sizeObject = sizeObject;
	}
	public void setSizeArray(JsonArray sizeArray){
		this.sizeArray = sizeArray;
	}
	/**
	 * set item picture JSON object
	 * @param picObject
	 */
	public void setPicJsonObject(JsonObject picObject){
		this.picObject = picObject;
		
	}
	/**
	 * @return the itemURL
	 */
	public String getItemURL() {
		return itemURL;
	}
	/**
	 * @param itemURL the itemURL to set
	 */
	public void setItemURL(String itemURL) {
		this.itemURL = itemURL;
	}
	/**
	 * save to csv file
	 * @return
	 */
	public String toCSV() {
		if (title != null)
			title = title.replace("\t", " ");
		if (prod_desc != null)
			prod_desc = prod_desc.replace("\t", " ");
		return String
				.format("%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s", title,
						prod_code, getBrand(), category, gender, colour, size,
						size_type, prod_desc, price, discount, pic, picWidth,
						picHeight);
	}
	/**
	 * set csv file header
	 * @return
	 */
	public static String getCSVHeader() {
		return String.format("%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s", "Title",
				"ProductCode", "Brand", "CategoryType", "Gender", "Color",
				"Size", "SizeType", "ProductDescription", "Price", "Discount",
				"Picture", "PicWidth", "PicHeight");
	}
	
	/**
	 * convert to JSON format
	 * @return
	 */
	public String toJson(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", itemURL);
		map.put("title", this.title);
		map.put("prod_code", this.prod_code);
		map.put("brand", this.brand);
		map.put("category", this.category);
		map.put("gender", this.gender);
		map.put("color", this.colour);
		map.put("size", this.sizeArray);
		map.put("price",this.price);
		map.put("discount", this.discount);
		map.put("pic", this.picObject);
		Gson gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting()
				.serializeNulls()
				.create();
		return gson.toJson(map);
		//return null;
	}
	
	
	/**
	 * Assuming CSV format: 0 Title 1 ProductCode 2 Brand 3 CategoryType 4
	 * Gender 5 Color 6 Size 7 SizeType 8 ProductDescription 9 Price 10 Discount
	 * 11 Picture 12 PicWidth 13 PicHeight
	 * 
	 * @param line
	 * @return
	 */
	public static Item parseItem(String line) {
		String[] segs = line.split("\t");
		Item item = new Item();
		item.setTitle(segs[0]);
		item.setBrand(segs[2]);
		item.setCategory(segs[3]);
		item.setGender(segs[4]);
		item.setColour(segs[5]);
		//item.setSize(segs[6]);
		item.setSizeType(segs[7]);
		item.setPrice(segs[9]);
		item.setDiscount(segs[10]);
		item.setPic(segs[11]);
		try {
			item.setPicWidth(Integer.parseInt(segs[12]));
			item.setPicHeight(Integer.parseInt(segs[13]));
		} catch (NumberFormatException e) {
			System.err.println(segs[12]);
			System.err.println(segs[13]);
		}
		return item;
	}

	public void setCategory(String string) {
		category = string;

	}

	public String getMaterial() {
		return material;
	}

	public void setBrandID(int brandID) {
		this.brandID = brandID;

	}

	public int getBrandID() {
		return brandID;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setAbstractItemID(int abstractItemID) {
		this.abstractItemID = abstractItemID;
	}

	public int getAbstractItemID() {
		return abstractItemID;
	}

	/**
	 * @return the sizeList
	 */
	public List<String> getSizeList() {
		return sizeList;
	}

	/**
	 * @param sizeList the sizeList to set
	 */
	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}

	/**
	 * @return the sizeAvalible
	 */
	public List<Boolean> getSizeAvalible() {
		return sizeAvalible;
	}

	/**
	 * @param sizeAvalible the sizeAvalible to set
	 */
	public void setSizeAvalible(List<Boolean> sizeAvalible) {
		this.sizeAvalible = sizeAvalible;
	}
}
