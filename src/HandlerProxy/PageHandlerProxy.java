package HandlerProxy;


import Model.Item;

/**
 * Handles a page.
 * 
 * @author Amos
 * 
 */
public interface PageHandlerProxy {

	/**
	 * Parse a single HTML page and extract an Item from it. The Item objects
	 * are DAOs and are relayed by the Data Access Layer to the Database
	 * 
	 * @param htmlText
	 * @return
	 */
	public Item parseItem(String url);

	/**
	 * Parse a list of items from an item list file
	 * 
	 * @param itemListFile
	 * @return
	 */
	//public List<Item> parseItems(File itemListFile);

}
