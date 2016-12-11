package wsd.com.wsd.models;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class Localization {
    @Getter
    private double latitude; // Szerokość Geograficzna
    @Getter
    private double longitude;// Długość Geograficzna
}
