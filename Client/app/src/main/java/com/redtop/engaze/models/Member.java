/**
 * 
 */
package com.redtop.engaze.models;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author ssunderk
 *
 */
public class Member {
	
	private String contactName;
	private List<ContactNumber> contactNumbers;
	private Bitmap image;
	private String imageUri;
	private List<EmailAddress> emailIds;
	
	public String getName() {
		return contactName;
	}
	public void setName(String name) {
		this.contactName = name;
	}
	public List<EmailAddress> getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(List<EmailAddress> emailIds) {
		this.emailIds = emailIds;
	}
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public List<ContactNumber> getContactNumbers() {
		return contactNumbers;
	}
	public void setContactNumbers(List<ContactNumber> contactNumbers) {
		this.contactNumbers = contactNumbers;
	}
}
