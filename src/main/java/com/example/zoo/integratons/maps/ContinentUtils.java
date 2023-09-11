package com.example.zoo.integratons.maps;

import com.example.zoo.enums.Continent;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class ContinentUtils {
    /**
     * This method is used to get a random predefined city from the continent
     * @param continent
     * @return
     */
    public String getLocation(Continent continent) {
        final List<String> data = continent.getCities();
        int randomElementIndex = ThreadLocalRandom.current().nextInt(continent.getCities().size()) % continent.getCities().size();
        return data.get(randomElementIndex);
    }
}
