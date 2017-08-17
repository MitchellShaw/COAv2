package Model;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/17/2017
 */
public class COA
{
    private String serialNumber;

    private COAOrder order;

    private int unitSerialNumber;

    /**
     * Getter for property 'serialNumber'.
     *
     * @return Value for property 'serialNumber'.
     */
    public String getSerialNumber()
    {
        return serialNumber;
    }

    /**
     * Setter for property 'serialNumber'.
     *
     * @param serialNumber Value to set for property 'serialNumber'.
     */
    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    /**
     * Getter for property 'order'.
     *
     * @return Value for property 'order'.
     */
    public COAOrder getOrder()
    {
        return order;
    }

    /**
     * Setter for property 'order'.
     *
     * @param order Value to set for property 'order'.
     */
    public void setOrder(COAOrder order)
    {
        this.order = order;
    }

    /**
     * Getter for property 'unitSerialNumber'.
     *
     * @return Value for property 'unitSerialNumber'.
     */
    public int getUnitSerialNumber()
    {
        return unitSerialNumber;
    }

    /**
     * Setter for property 'unitSerialNumber'.
     *
     * @param unitSerialNumber Value to set for property 'unitSerialNumber'.
     */
    public void setUnitSerialNumber(int unitSerialNumber)
    {
        this.unitSerialNumber = unitSerialNumber;
    }
}
