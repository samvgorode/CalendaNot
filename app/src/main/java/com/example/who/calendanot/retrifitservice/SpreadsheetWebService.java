package com.example.who.calendanot.retrifitservice;


import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by who on 26.11.2016.
 */

public interface SpreadsheetWebService {
        @FormUrlEncoded
        @POST("1FAIpQLScBtzWshFf7dpZgdS0-MFOAjidJgm0TucfuIYroJBOoLo6yrA/formResponse")
        public void completeQuestionnaire(
            @Field("entry.903290059") String companyInput,
            @Field("entry.1513554788") String nameSurnameInput,
            @Field("entry.1390012648") String positionInput,
            @Field("entry.1916287616") String telephoneInput,
            @Field("entry.177284360") String emailInput,
            @Field("entry.1625445229") String restaurantInput,
            @Field("entry.1288602328") String coordinatesInput,
            @Field("entry.572041316") String themeInput,
            @Field("entry.1034214338") String detailsInput,
            Callback<Response> callback
        );

}
