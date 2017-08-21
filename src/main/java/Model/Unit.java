package Model;

import javax.persistence.*;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
@Entity
public class Unit
{
    @Id
    @Column(unique = true)
    private String serialNumber;

    private String productID;

    private String modelNumber;

    @Column(unique = true)
    private int scheduleNumber;

    @ManyToOne
    @JoinColumn(name = "orderNumber")
    private Order order;

    @OneToOne
    @JoinColumn(name = "COA", unique = true)
    private COA coa;

    /**
     * Getter for property 'coaOrder'.
     *
     * @return Value for property 'coaOrder'.
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Setter for property 'coaOrder'.
     *
     * @param order Value to set for property 'coaOrder'.
     */
    public void setOrder(Order order)
    {
        this.order = order;
    }

    /**
     * Getter for property 'scheduleNumber'.
     *
     * @return Value for property 'scheduleNumber'.
     */
    public int getScheduleNumber()
    {
        return scheduleNumber;
    }

    /**
     * Setter for property 'scheduleNumber'.
     *
     * @param scheduleNumber Value to set for property 'scheduleNumber'.
     */
    public void setScheduleNumber(int scheduleNumber)
    {
        this.scheduleNumber = scheduleNumber;
    }

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
     * Getter for property 'productID'.
     *
     * @return Value for property 'productID'.
     */
    public String getProductID()
    {
        return productID;
    }

    /**
     * Setter for property 'productID'.
     *
     * @param productID Value to set for property 'productID'.
     */
    public void setProductID(String productID)
    {
        this.productID = productID;
    }

    /**
     * Getter for property 'coa'.
     *
     * @return Value for property 'coa'.
     */
    public COA getCoa()
    {
        return coa;
    }

    /**
     * Setter for property 'coa'.
     *
     * @param coa Value to set for property 'coa'.
     */
    public void setCoa(COA coa)
    {
        this.coa = coa;
    }

    /**
     * Getter for property 'modelNumber'.
     *
     * @return Value for property 'modelNumber'.
     */
    public String getModelNumber()
    {
        return modelNumber;
    }

    /**
     * Setter for property 'modelNumber'.
     *
     * @param modelNumber Value to set for property 'modelNumber'.
     */
    public void setModelNumber(String modelNumber)
    {
        this.modelNumber = modelNumber;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Unit{" +
                "serialNumber='" + serialNumber + '\'' +
                ", productID='" + productID + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", scheduleNumber=" + scheduleNumber +
                ", coa=" + coa +
                ", order=" + order +
                '}';
    }
}