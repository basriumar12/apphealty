package app.manual;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aisatriani on 21/08/15.
 */
public class AppConfig {
    public static final String INTENT_IDTEMPAT = "intent_idtempat";

    private final static double lat = 0.543544;
    private final static double lon = 123.056769;
    public static final String INTENT_KATEGORI = "intent_kategori";

    public static LatLng getDefaultLatLng(){
        return new LatLng(lat,lon);
    }
}
