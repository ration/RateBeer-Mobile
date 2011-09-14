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
package dk.moerks.ratebeermobile.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RatingData {
	private String id;
	
	private int aroma;
	private int appearance;
	private int flavor;
	private int palate;
	private int overall;
	@SuppressWarnings("unused")
	private int totalscore;
	private String comment;
	
	public RatingData(String id, int aroma, 
			int appearance, int flavor, int palate, int overall, String comment) {
		this.id = id;
		this.aroma = aroma;
		this.appearance = appearance;
		this.flavor = flavor;
		this.palate = palate;
		this.overall = overall;
		this.comment = comment;
	}
	public RatingData() {
		
	}
	
	public List<NameValuePair >toNameValuePair() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		parameters.add(new BasicNameValuePair("BeerID", id));  
		parameters.add(new BasicNameValuePair("aroma", String.valueOf(aroma)));
		parameters.add(new BasicNameValuePair("appearance", String.valueOf(appearance)));  
		parameters.add(new BasicNameValuePair("flavor", String.valueOf(flavor)));  
		parameters.add(new BasicNameValuePair("palate", String.valueOf(palate)));  
		parameters.add(new BasicNameValuePair("overall", String.valueOf(overall)));
		parameters.add(new BasicNameValuePair("totalscore", getTotalscore()));
		parameters.add(new BasicNameValuePair("Comments", comment));
		return parameters;
	}
	
	
	public int getAroma() {
		return aroma;
	}
	public void setAroma(int aroma) {
		this.aroma = aroma;
	}
	public int getAppearance() {
		return appearance;
	}
	public void setAppearance(int appearance) {
		this.appearance = appearance;
	}
	public int getFlavor() {
		return flavor;
	}
	public void setFlavor(int flavor) {
		this.flavor = flavor;
	}
	public int getPalate() {
		return palate;
	}
	public void setPalate(int palate) {
		this.palate = palate;
	}
	public int getOverall() {
		return overall;
	}
	public void setOverall(int overall) {
		this.overall = overall;
	}
	public String getTotalscore() {
		int total = (aroma + appearance + flavor + palate + overall);

		float totalscore =  ((float)total) / 10;
		String result = "" + totalscore;
		return result;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
