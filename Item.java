/*
 *The Item class represents a product with various attributes such as SKU, 
 * name, category, quantity, prices, and discounts.
 */

public class Item {

    private String sku; //unique identifier of item
    private String name; //name of item
    private String category; //category of item
    private int quantity; //quantity of item in stock
    private int minimumQuantity; //minimum quantity required to maintain in stock
    private double vendorPrice; //price at which vendor sells the item
    private double markupPercentage; //percentage markup for vendor price
    private double regularPrice; //regular selling price of item
    private double currentDiscount; //current discount 
    private double currentPrice; //current price of item

    //constructor to initialize an Item object with all attributes
    public Item(String sku, String name, String category, int quantity, int minimumQuantity, double vendorPrice,
            double markupPercentage, double regularPrice, double currentDiscount, double currentPrice) 
    {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.vendorPrice = vendorPrice;
        this.markupPercentage = markupPercentage;
        this.regularPrice = regularPrice;
        this.currentDiscount = currentDiscount;
        this.currentPrice = currentPrice;
    }

    //A string containing all attributes of the item separated by commas.

    public String toString() 
    {
        return sku + "," + name + "," + category + "," + quantity + "," + minimumQuantity + "," + vendorPrice + ","
                + markupPercentage + "," + regularPrice + "," + currentDiscount + "," + currentPrice;
    }

    //getters and setters methods for all attributes

    /**
     * @return String return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return int return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return int return the minimumQuantity
     */
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     * @param minimumQuantity the minimumQuantity to set
     */
    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    /**
     * @return double return the vendorPrice
     */
    public double getVendorPrice() {
        return vendorPrice;
    }

    /**
     * @param vendorPrice the vendorPrice to set
     */
    public void setVendorPrice(double vendorPrice) {
        this.vendorPrice = vendorPrice;
    }

    /**
     * @return double return the markupPercentage
     */
    public double getMarkupPercentage() {
        return markupPercentage;
    }

    /**
     * @param markupPercentage the markupPercentage to set
     */
    public void setMarkupPercentage(double markupPercentage) {
        this.markupPercentage = markupPercentage;
    }

    /**
     * @return double return the regularPrice
     */
    public double getRegularPrice() {
        return regularPrice;
    }

    /**
     * @param regularPrice the regularPrice to set
     */
    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    /**
     * @return double return the currentDiscount
     */
    public double getCurrentDiscount() {
        return currentDiscount;
    }

    /**
     * @param currentDiscount the currentDiscount to set
     */
    public void setCurrentDiscount(double currentDiscount) {
        this.currentDiscount = currentDiscount;
    }

    /**
     * @return double return the currentPrice
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @param currentPrice the currentPrice to set
     */
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

}