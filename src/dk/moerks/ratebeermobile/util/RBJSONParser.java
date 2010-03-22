/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.vo.PlacesInfo;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class RBJSONParser {
	private static final String LOGTAG = "RBJSONParser";
	
	public static List<PlacesInfo> parsePlaces(String responseString) throws RBParserException {
		ArrayList<PlacesInfo> results = new ArrayList<PlacesInfo>();

		try {
			Log.d(LOGTAG, "Creating JSON Object");
			JSONArray jsonObjects = new JSONArray(responseString);
			
			Log.d(LOGTAG, "ARRAY LENGTH: " + jsonObjects.length());
			for (int i = 0; i < jsonObjects.length(); i++) {
				PlacesInfo place = new PlacesInfo();
				
				Log.d(LOGTAG, "JSONObject("+i+"): " + jsonObjects.get(i));
				JSONObject json = new JSONObject(jsonObjects.getString(i));
				
				//{"PlaceID":6996,"PlaceName":"Lush Wine and Spirits","PlaceType":"3","Address":"1257 S. Halsted St.",
				//"City":"Chicago","StateID":14,"CountryID":213,"PostalCode":"60607","PhoneNumber":"738-1900","AvgRating":33.5,
				//"PhoneAC":"312","Latitude":41.865794,"Longitude":-87.646683,"Distance":1.368293}
				place.setPlaceID(json.getString("PlaceID"));
				place.setPlaceName(json.getString("PlaceName"));
				place.setPlaceType(json.getString("PlaceType"));
				place.setAddress(json.getString("Address"));
				place.setCity(json.getString("City"));
				place.setStateID(json.getString("StateID"));
				place.setCountryID(json.getString("CountryID"));
				place.setPostalCode(json.getString("PostalCode"));
				place.setPhoneNumber(json.getString("PhoneNumber"));
				place.setAvgRating(json.getString("AvgRating"));
				place.setPhoneAC(json.getString("PhoneAC"));
				place.setLatitude(json.getString("Latitude"));
				place.setLongitude(json.getString("Longitude"));
				place.setDistance(json.getString("Distance"));
				
				results.add(place);
			}
			
			return results;
		} catch(JSONException e){
			throw new RBParserException(LOGTAG, "Unable to parse places", e);
		}
	}
	
	public static List<SearchResult> parseSearch(String responseString) throws RBParserException {
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		
		try {
			Log.d(LOGTAG, "Creating JSON Object");
			JSONArray jsonObjects = new JSONArray(responseString);
			
			Log.d(LOGTAG, "ARRAY LENGTH: " + jsonObjects.length());
			for (int i = 0; i < jsonObjects.length(); i++) {
				SearchResult searchResult = new SearchResult();
				
				//"BeerID":106264,"BeerName":"Skands 5 \u00C5r","BrewerID":4147,"OverallPctl":52,"RateCount":17,"IsAlias":false,"Retired":false,"State":" ","Country":"Denmark ","IsRated":0
				Log.d(LOGTAG, "JSONObject("+i+"): " + jsonObjects.get(i));
				JSONObject json = new JSONObject(jsonObjects.getString(i));
				
				searchResult.setBeerId(json.getString("BeerID"));
				searchResult.setBeerName(json.getString("BeerName"));
				searchResult.setBeerPercentile(json.getString("OverallPctl"));
				searchResult.setBeerRatings(json.getString("RateCount"));
				searchResult.setRated(json.getString("IsRated").equals("1"));
				searchResult.setAlias(json.getBoolean("IsAlias"));
				searchResult.setRetired(json.getBoolean("Retired"));
				
				results.add(searchResult);
			}
		} catch(JSONException e){
			throw new RBParserException(LOGTAG, "Unable to parse search", e);
		}
		
		return results;
	}
}