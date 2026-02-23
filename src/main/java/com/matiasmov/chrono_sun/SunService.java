package com.matiasmov.chrono_sun;

import com.matiasmov.chrono_sun.dto.GeocodingResponse;
import com.matiasmov.chrono_sun.dto.SunDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import java.util.Locale;
import java.util.Map;

@Service
public class SunService {

    private final RestClient restClient = RestClient.create();

    @Value("${app.user-agent.email}")
    private String userEmail;

    @Value("${app.timezone.key}")
    private String timeZoneApiKey;

    public SunDataResponse getSunDataByAddress(String address) {
        String userAgent = "ChronoSunApp/1.0 (" + userEmail + ")";

    
        GeocodingResponse[] locations = restClient.get()
            .uri("https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1", address)
            .header("User-Agent", userAgent) 
            .retrieve()
            .body(GeocodingResponse[].class);

        if (locations != null && locations.length > 0) {
            String latStr = locations[0].lat();
            String lngStr = locations[0].lon();

            SunDataResponse sunData = restClient.get()
                .uri("https://api.sunrise-sunset.org/json?lat={lat}&lng={lng}&formatted=0", latStr, lngStr)
                .retrieve()
                .body(SunDataResponse.class);

            if (sunData != null) {
                double lat = Double.parseDouble(latStr);
                double lng = Double.parseDouble(lngStr);
                String timeZone = getTimeZone(lat, lng);
                
                return new SunDataResponse(sunData.results(), sunData.status(), timeZone);
            }
            return null;
        }

        return null;
    }

    public String getTimeZone(double lat, double lng) {
        
        String url = String.format(Locale.US, 
            "https://api.timezonedb.com/v2.1/get-time-zone?key=%s&format=json&by=position&lat=%f&lng=%f",
            timeZoneApiKey, lat, lng
        );

        try {
            RestTemplate restTemplate = new RestTemplate();
            var response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("zoneName")) {
                return response.get("zoneName").toString();
            }
            return "UTC";
        } catch (Exception e) {
            System.err.println("Error searching for time zone: " + e.getMessage());
            return "UTC";
        }
    }
}