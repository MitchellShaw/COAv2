package Model;

import javax.persistence.*;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
@Entity
public class Unit
{
    /**
     * Serial number that is assigned to unit used to uniquely identify between units.
     */
    @Id
    @Column(unique = true)
    private String serialNumber;

    /**
     * Product ID is the unit MC, example: 7761MC100
     */
    private String productID;

    /**
     * 4x4x4 number used to describe the unit
     */
    private String modelNumber;

    /**
     * Schedule number is used for ERP purposes, can be used to uniquely identify units.
     */
    @Column(unique = true)
    private int scheduleNumber;

    /**
     * Order object the unit was assigned to
     */
    @ManyToOne
    @JoinColumn(name = "orderNumber")
    private Order order;

    /**
     * The COA object that was attached to this unit
     */
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