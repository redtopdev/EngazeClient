package com.redtop.engaze.interfaces;

import com.redtop.engaze.utils.Constants.Action;

public interface OnActionFailedListner {
	void actionFailed(String msg, Action action);

}
