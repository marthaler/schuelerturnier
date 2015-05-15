/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu;


import ch.emad.model.schuetu.model.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Controller zum Asynchronen sichern auf dem Hauptserver
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@Component
public class SaveController {

    private static final Logger LOG = Logger.getLogger(SaveController.class);

    @Autowired
    WebcamBusiness webcamBusiness;

    @Async
    public void save(Spiel spiel, byte[] rawPicture) {
        webcamBusiness.save(spiel, rawPicture);
    }


}
                        