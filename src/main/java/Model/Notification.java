package Model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Created by Ramon Johnson
 * 2017-09-11.
 */
public class Notification
{
    public Notification(String _title, String _message, Pos _pos)
    {
        Notifications notification = Notifications.create()
                .darkStyle()
                .title(_title)
                .graphic(new ImageView(new Image(getClass().getResource("/Images/ncr.bmp").toExternalForm())))
                .text(_message)
                .hideAfter(Duration.seconds(7))
                .position(_pos);
        notification.show();
    }
}
