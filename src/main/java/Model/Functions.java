package Model;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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

    /**
     * @param _node Label object to be used to setup ToolTip
     * @param _text The ToolTip message
     */
    public static void setToolTip(Label _node, String _text)
    {
        _node.setTooltip(new Tooltip(_text));
    }

    /**
     * @param _node TextField object to be used to setup ToolTip
     * @param _text The ToolTip message
     */
    public static void setToolTip(TextField _node, String _text)
    {
        _node.setTooltip(new Tooltip(_text));
    }

    /**
     * @param _node Button object to be used to setup ToolTip
     * @param _text The ToolTip message
     */
    public static void setToolTip(Button _node, String _text)
    {
        _node.setTooltip(new Tooltip(_text));
    }

    /**
     * @param _tf TextFieldObject that needs to be numeric with serial number standards digits
     * @param _button The button that will be disabled or enabled based on the result on the listener
     */
    public static void serialNumberTextFieldTiedToButton(TextField _tf, Button _button)
    {
        _tf.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.length() > 6)
            {
                if (!newValue.matches("(\\d{7,8})|(\\d{2}-\\d{0,8})"))
                {
                    Platform.runLater(() -> _tf.setText(""));
                    _button.setDisable(true);
                } else
                    _button.setDisable(false);
            }
            else
                _button.setDisable(true);
        });
    }

    /**
     * @param _tf TextFieldObject that needs to be numeric with serial number standards digits
     * @param _button The button that will be disabled or enabled based on the result on the listener
     */
    public static void scheduleNumberTextFieldTiedToButton(TextField _tf, Button _button)
    {
        _tf.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.length() > 6)
            {
                if (!newValue.matches("(\\d{7,8})"))
                {
                    _tf.setText(oldValue);
                    _button.setDisable(true);
                }
                else
                    _button.setDisable(false);
            }
        });
    }

    /**
     * @param _tx Textfield that will be forced to be numeric
     * @param _min The minimum amount of characters required
     */
    public static void numericTextFieldWithMinimum(TextField _tx, int _min)
    {
        _tx.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(newValue.length() >= _min)
            {
                if (!newValue.matches("\\d{1," + _min + "}")) {
                    _tx.setText(newValue.replaceAll("\\D", ""));
                }
            }
        }));
    }
}
