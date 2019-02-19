package com.redtop.engaze.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.interfaces.OnAPICallCompleteListner;

public class ContactAndGroupListManager {
    private static Context mContext;
    private final static String TAG = AppUtility.class.getName();

    public static void cacheContactAndGroupList(Context context) {
        try {
            mContext = context;
            AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, false, mContext);
            AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZED, false, mContext);
            AppUtility.setPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZATION_FAILED, false, mContext);
            AppUtility.setPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZED, false, mContext);
            ArrayList<ContactOrGroup> contacts = getAllContactsFromDeviceContactList();
            if (contacts != null && contacts.size() > 0) {
                InternalCaching.saveContactListToCache(contacts, mContext);
                broadcast_ContactList_Refreshed(true, Constants.CONTACTS, "");
                AppUtility.setPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZED, true, mContext);
                cacheRegisteredContacts(contacts);
            } else {
                AppUtility.setPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
                AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
                broadcast_ContactList_Refreshed(false, Constants.CONTACTS, "");

            }
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
            ex.printStackTrace();
            AppUtility.setPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
            AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
            broadcast_ContactList_Refreshed(false, Constants.CONTACTS, "");
        }
    }

    public static ContactOrGroup getContact(Context context, String userId) {
        ContactOrGroup cg = null;
        Hashtable<String, ContactOrGroup> table = InternalCaching.getRegisteredContactListFromCache(context);
        if (table != null) {
            cg = table.get(userId);
        }

        return cg;
    }

    public static void assignContactsToEventMembers(ArrayList<EventMember> eventMembers, Context context) {
        Hashtable<String, ContactOrGroup> registeredList = InternalCaching.getRegisteredContactListFromCache(context);
        ContactOrGroup cg;
        for (EventMember mem : eventMembers) {
            cg = mem.getContact();
            if (cg == null) {
                cg = registeredList.get(mem);
                if (cg == null) {
                    cg = new ContactOrGroup();
                    cg.setIconImageBitmap(ContactOrGroup.getAppUserIconBitmap(context));
                    if (AppUtility.isParticipantCurrentUser(mem.getUserId(), context) || mem.getProfileName().startsWith("~")) {
                        cg.setImageBitmap(AppUtility.generateCircleBitmapForText(context, AppUtility.getMaterialColor(mem.getProfileName()), 40, mem.getProfileName().substring(1, 2).toUpperCase()));
                    } else {
                        cg.setImageBitmap(AppUtility.generateCircleBitmapForText(context, AppUtility.getMaterialColor(mem.getProfileName()), 40, mem.getProfileName().substring(0, 1).toUpperCase()));
                    }
                }
                mem.setContact(cg);
            }
        }
    }

    public static ArrayList<ContactOrGroup> sortContacts(ArrayList<ContactOrGroup> contactsAndGroups) {
        if (contactsAndGroups.size() > 0) {
            Collections.sort(contactsAndGroups, new Comparator<ContactOrGroup>() {

                @Override
                public int compare(ContactOrGroup lhs, ContactOrGroup rhs) {

                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }

            });
        }
        return contactsAndGroups;
    }

    public static ArrayList<ContactOrGroup> getAllRegisteredContacts(Context context) {
        ArrayList<ContactOrGroup> contactsAndGroups = new ArrayList<ContactOrGroup>(InternalCaching.getRegisteredContactListFromCache(context).values());

        return sortContacts(contactsAndGroups);
    }

    public static ArrayList<ContactOrGroup> getAllContacts(Context context) {
        ArrayList<ContactOrGroup> contactsAndGroups = new ArrayList(InternalCaching.getContactListFromCache(context));
        ArrayList<ContactOrGroup> registered = new ArrayList<ContactOrGroup>();
        ArrayList<ContactOrGroup> unRegistered = new ArrayList<ContactOrGroup>();
        ArrayList<ContactOrGroup> finalContacts = new ArrayList<ContactOrGroup>();

        for (ContactOrGroup cg : contactsAndGroups) {
            if (cg.getUserId() != null) {
                registered.add(cg);
            } else {
                unRegistered.add(cg);
            }
        }

        finalContacts.addAll(sortContacts(registered));
        finalContacts.addAll(sortContacts(unRegistered));
        return finalContacts;
    }

    public static ArrayList<ContactOrGroup> getAllContactsFromCache(Context context) {
        return InternalCaching.getContactListFromCache(context);
    }

    public static ArrayList<ContactOrGroup> getGroups(Context context) {
        return AppUtility.getPrefArrayList("Groups", context);
    }

    public static void initializedRegisteredUser(Context context) {
        cacheRegisteredContacts(getAllContactsFromCache(context));
    }

    private static void cacheRegisteredContacts(final ArrayList<ContactOrGroup> contactsAndgroups) {
        if (!AppUtility.isNetworkAvailable(mContext)) {
            String message = mContext.getResources().getString(R.string.message_general_no_internet_responseFail);
            //Toast.makeText(mContext,	message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, message);
            AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
            broadcast_ContactList_Refreshed(false, Constants.CONTACTS, message);
            return;
        }

        APICaller.AssignUserIdToRegisteredUser(mContext, contactsAndgroups, new OnAPICallCompleteListner() {
            @Override
            public void apiCallComplete(JSONObject response) {
                try {
                    String Status = (String) response.getString("Status");
                    if (Status == "true") {
                        Hashtable<String, ContactOrGroup> registeredContacts = prepareRegisteredContactList(response, contactsAndgroups);
                        InternalCaching.saveRegisteredContactListToCache(registeredContacts, mContext);
                        InternalCaching.saveContactListToCache(contactsAndgroups, mContext);
                        AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZED, true, mContext);
                        broadcast_ContactList_Refreshed(true, Constants.REGISTERED_CONTACTS, "");
                    } else {
                        String error = (String) response.getString("ErrorMessage");
                        Log.d(TAG, error);
                        AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
                        broadcast_ContactList_Refreshed(false, Constants.REGISTERED_CONTACTS, "");
                    }

                } catch (Exception ex) {
                    Log.d(TAG, ex.toString());
                    ex.printStackTrace();
                    broadcast_ContactList_Refreshed(false, Constants.REGISTERED_CONTACTS, "");
                    AppUtility.setPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, true, mContext);
                }

            }
        }, new OnAPICallCompleteListner() {

            @Override
            public void apiCallComplete(JSONObject response) {
                broadcast_ContactList_Refreshed(false, Constants.REGISTERED_CONTACTS, "");

            }
        });
    }

    private static Hashtable<String, ContactOrGroup> prepareRegisteredContactList(JSONObject response, ArrayList<ContactOrGroup> contactsAndGroups) throws JSONException, IOException, ClassNotFoundException {


        Hashtable<String, ContactOrGroup> contacts = new Hashtable<String, ContactOrGroup>();
        for(ContactOrGroup c : contactsAndGroups){
            for (String mobileNo : c.getMobileNumbers()){
                contacts.put(mobileNo,c);
            }
        }
        Hashtable<String, ContactOrGroup> registeredContacts = new Hashtable<String, ContactOrGroup>();
        JSONArray jUsers = (JSONArray) response.getJSONArray("ListOfRegisteredContacts");
        if (jUsers.length() == 0) {
            return registeredContacts;
        }
        String userId = "";
        String phoneNumber = "";
        ContactOrGroup cg = null;
        for (int i = 0, size = jUsers.length(); i < size; i++) {
            JSONObject jsonObj = jUsers.getJSONObject(i);
            phoneNumber = jsonObj.getString("MobileNumberStoredInRequestorPhone");
            cg = contacts.get(jsonObj.getString("MobileNumberStoredInRequestorPhone"));
            if (cg != null) {
                userId = jsonObj.get("UserId").toString();
                cg.setUserId(userId);
                registeredContacts.put(userId, cg);
            }
        }
        return registeredContacts;
    }

    private static ArrayList<ContactOrGroup> getAllContactsFromDeviceContactList() {
        Cursor cursor = null;
        ArrayList<ContactOrGroup> contacts = new ArrayList<ContactOrGroup>();
        try {
            ContactOrGroup cg;
            String[] columns = {ContactsContract.Contacts.PHOTO_THUMBNAIL_URI, ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
            cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, columns, null, null, null);

            int ColumeIndex_THUMBNAIL = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
            int ColumeIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int ColumeIndex_DISPLAY_NAME = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int ColumeIndex_HAS_PHONE_NUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

            //Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DESC");


            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    cg = new ContactOrGroup();
                    String thumbnail_uri = cursor.getString(ColumeIndex_THUMBNAIL);
                    String has_phone = cursor.getString(ColumeIndex_HAS_PHONE_NUMBER);
                    if (!has_phone.endsWith("0")) {
                        ArrayList<String> phoneNumbers = getPhoneNumbers(cursor.getString(ColumeIndex_ID));
                        cg.setMobileNumbers(phoneNumbers);
                        if(phoneNumbers.size() > 0) {
                            cg.setMobileNumber(phoneNumbers.get(0));
                        }
                        cg.setName(cursor.getString(ColumeIndex_DISPLAY_NAME));
                        cg.setThumbnailUri(thumbnail_uri);
                        if (cg.getThumbnailUri() == null) {
                            cg.setIconImageBitmap(ContactOrGroup.getAppUserIconBitmap(mContext));
                            String startingchar = cg.getName().substring(0, 1);
                            if (!(startingchar.matches("[0-9]") || startingchar.startsWith("+"))) {
                                cg.setImageBitmap(AppUtility.generateCircleBitmapForText(mContext, AppUtility.getMaterialColor(cg.getName()), 40, startingchar.toUpperCase()));
                            } else {
                                cg.setImageBitmap(AppUtility.generateCircleBitmapForIcon(mContext, AppUtility.getMaterialColor(cg.getName()), 40, Uri.parse("android.resource://com.redtop.engaze/drawable/ic_person_white_24dp")));
                            }
                        } else {
                            Bitmap pofilePicBitmap = AppUtility.generateCircleBitmapForImage(mContext, 54, Uri.parse(cg.getThumbnailUri()));
                            cg.setImageBitmap(pofilePicBitmap);
                            cg.setIconImageBitmap(pofilePicBitmap);

                        }
                        contacts.add(cg);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contacts;
    }

    private static ArrayList<String> getPhoneNumbers(String id) {
        ArrayList<String> numbers = new ArrayList<String>();
        Cursor phones = null;
        try {
            //Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + id, null, null);
            //Cursor phones = mContext.getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + id, null, null);
            phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{id}, null);
            while (phones.moveToNext()) {

                numbers.add(phones.getString(phones.getColumnIndex(Phone.NUMBER)).replaceAll("\\s", ""));
            }


        }
        catch(Exception ex)
        {

        }
        finally {
            phones.close();
        }
        return numbers;
    }

    private static ArrayList<ContactOrGroup> getAllGroups() {

        ArrayList<ContactOrGroup> groups = new ArrayList<ContactOrGroup>();
        new ContactOrGroup("group", 123, null);

        return groups;
    }

    private static void broadcast_ContactList_Refreshed(Boolean iSSuccess, String contactType, String message) {
        Intent intent;

        if (iSSuccess) {
            if (contactType.equals(Constants.CONTACTS)) {
                intent = new Intent(Constants.CONTACT_LIST_INITIALIZATION_SUCCESS);
            } else {
                intent = new Intent(Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_SUCCESS);
            }
        } else {
            if (contactType.equals(Constants.CONTACTS)) {
                intent = new Intent(Constants.CONTACT_LIST_INITIALIZATION_FAILED);
            } else {
                intent = new Intent(Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED);
            }

        }
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}