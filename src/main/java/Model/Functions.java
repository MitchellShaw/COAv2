package Model;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
public class Functions
{

    /**
     * @param _stage Stage object to set the Icon to ncr logo
     */
    public static void setUpIcons(Stage _stage)
    {
        _stage.getIcons().add(new Image("/Images/ncr.png"));
    }
}
