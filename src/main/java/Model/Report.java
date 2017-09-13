package Model;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 9/13/2017
 */
public class Report
{
    private String partNumber;

    private String quantity;

    public Report(String partNumber, String quantity)
    {
        this.partNumber = partNumber;
        this.quantity = quantity;
    }

    /**
     * Getter for property 'partNumber'.
     *
     * @return Value for property 'partNumber'.
     */
    public String getPartNumber()
    {
        return partNumber;
    }

    /**
     * Setter for property 'partNumber'.
     *
     * @param partNumber Value to set for property 'partNumber'.
     */
    public void setPartNumber(String partNumber)
    {
        this.partNumber = partNumber;
    }

    /**
     * Getter for property 'quantity'.
     *
     * @return Value for property 'quantity'.
     */
    public String getQuantity()
    {
        return quantity;
    }

    /**
     * Setter for property 'quantity'.
     *
     * @param quantity Value to set for property 'quantity'.
     */
    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
}
