package com.example.krystian892.truss;

import android.view.MotionEvent;

interface Menu_OnTouch {
	int onTouchOnMenu(MotionEvent e, int action);
	boolean isErasingOn();
}
