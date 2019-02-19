package com.redtop.engaze.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redtop.engaze.entity.UsersLocationDetail;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;

public class RunningEventHelper {	
	public static void arrangeListinAvailabilityOrder(List<UsersLocationDetail> usersLocationDetailList) {

		Collections.sort(usersLocationDetailList, new Comparator<UsersLocationDetail>()
				{

			@Override
			public int compare(UsersLocationDetail lhs, UsersLocationDetail rhs) {
				if(lhs.getAcceptanceStatus()==rhs.getAcceptanceStatus()){
					if (lhs.getAcceptanceStatus()==AcceptanceStatus.ACCEPTED ){
						if(lhs.currentAddress!=null && lhs.currentAddress!=""){
							if(rhs.currentAddress!=null && rhs.currentAddress!=""){
								return  lhs.getUserName().compareToIgnoreCase(rhs.getUserName());
							}
							else {
								return -1;
							}

						}
						else {
							if(rhs.currentAddress!=null && rhs.currentAddress!=""){
								return 1;
							}
							else
							{
								return  lhs.getUserName().compareToIgnoreCase(rhs.getUserName());
							}
						}
					}

					return  lhs.getUserName().compareToIgnoreCase(rhs.getUserName());
				}
				else {
					if (lhs.getAcceptanceStatus()==AcceptanceStatus.ACCEPTED ){
						return -1;
					}
					else
					{
						return 1;
					}
				}
			}				
		});
	}

}


