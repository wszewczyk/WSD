package wsd.com.wsd.models;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Localization {
    @Getter
    @Setter
    private double latitude; // Szerokość Geograficzna
    @Getter
    @Setter
    private double longitude;// Długość Geograficzna
    @Getter
    @Setter
    private String localizationString; //lokalizacja "slownie" - tak lokalizacje wydarzenia definiuje Google Calendar

    public Localization(double lat, double lon)
    {
        latitude = lat;
        longitude = lon;
    }

    public Localization(String local)
    {
        localizationString = local;
    }

}
