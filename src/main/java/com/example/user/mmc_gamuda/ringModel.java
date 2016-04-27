package com.example.user.mmc_gamuda;

import com.shaded.fasterxml.jackson.annotation.JsonAutoDetect;
import com.shaded.fasterxml.jackson.annotation.JsonIgnore;
import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by user on 9/17/15.
 */
class ringModel {


    boolean panel;
    boolean emergencyPhone;
    boolean tnlight;


@JsonIgnore
    public ringModel(){

    }
    @JsonProperty("tnlight")
    public boolean gettnlight(){

        return tnlight;
    }

    @JsonProperty("panel")
    public boolean getpanel(){

        return panel;
    }

    @JsonProperty("emergencyPhone")
    public boolean getemergencyPhone(){

        return emergencyPhone;
    }

    public boolean get(String equipment){

        if(equipment == "Tunnel Light"){
            return gettnlight();
        }

        if(equipment == "Panel"){
            return getpanel();
        }

        if(equipment == "Emergency Phone"){
            return getemergencyPhone();
        }

        return false;
    }

}
