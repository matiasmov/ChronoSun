package com.matiasmov.chrono_sun;

import com.matiasmov.chrono_sun.dto.GeocodingResponse;
import com.matiasmov.chrono_sun.dto.SunDataResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SunService {

 private final RestClient restClient = RestClient.create();

    @Value("${app.user-agent.email}")
    private String userEmail;

    public SunDataResponse getSunDataByAddress(String address) {
       
        String userAgent = "ChronoSunApp/1.0 (" + userEmail + ")";

        GeocodingResponse[] locations = restClient.get()
            .uri("https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1", address)
            .header("User-Agent", userAgent) 
            .retrieve()
            .body(GeocodingResponse[].class);

        if (locations != null && locations.length > 0) {
            String lat = locations[0].lat();
            String lng = locations[0].lon();

		
		return restClient.get()
                .uri("https://api.sunrise-sunset.org/json?lat={lat}&lng={lng}&formatted=0&tzid=America/Sao_Paulo", lat, lng)
                .retrieve()
                .body(SunDataResponse.class);


		}

		return null;
	}
}
