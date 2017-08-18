package Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
@Entity
public class Operator
{
    /**
     * Variable to hol the RQSID of the operator
     */
    @Id
    @Column(unique = true)
    private String operator;

    /**
     * First name of the operator
     */
    private String firstName;

    /**
     * Last name of the operator
     */
    private String lastName;
    @OneToMany(mappedBy = "operatorID")
    private List<COA> coaList;

    /**
     * Getter for property 'rqsID'.
     *
     * @return Value for property 'rqsID'.
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * Setter for property 'rqsID'.
     *
     * @param rqsID Value to set for property 'rqsID'.
     */
    public void setOperator(String rqsID)
    {
        this.operator = rqsID;
    }

    /**
     * Getter for property 'firstName'.
     *
     * @return Value for property 'firstName'.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Setter for property 'firstName'.
     *
     * @param firstName Value to set for property 'firstName'.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Getter for property 'lastName'.
     *
     * @return Value for property 'lastName'.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Setter for property 'lastName'.
     *
     * @param lastName Value to set for property 'lastName'.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Getter for property 'coaList'.
     *
     * @return Value for property 'coaList'.
     */
    public List<COA> getCoaList()
    {
        return coaList;
    }

    /**
     * Setter for property 'coaList'.
     *
     * @param coaList Value to set for property 'coaList'.
     */
    public void setCoaList(List<COA> coaList)
    {
        this.coaList = coaList;
    }
}
