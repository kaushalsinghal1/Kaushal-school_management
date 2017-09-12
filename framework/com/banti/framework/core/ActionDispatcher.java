package com.banti.framework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActionDispatcher {
	private static Map<String, List<ClientListener>> responseListener = Collections
			.synchronizedMap(new HashMap<String, List<ClientListener>>());
	private ActionDispatcher(){
		
	}

	public static void registeredListener(String commandCode,
			ClientListener listener) {
		List<ClientListener> list = responseListener.get(commandCode);
		if (list == null) {
			list = new ArrayList<ClientListener>();
			responseListener.put(commandCode, list);
		}
		list.add(listener);
	}

	public static void removeListener(String commandCode) {
		responseListener.remove(commandCode);
	}

	public static void fireAction(String commandCode,Object obj) {
		List<ClientListener> list = responseListener.get(commandCode);
		if (list == null) {
			return;
		}
		for (Iterator<ClientListener> iterator = list.iterator(); iterator
				.hasNext();) {
			ClientListener clientListener = iterator.next();
			clientListener.fireAction(commandCode,obj);
		}
		removeListener(commandCode);
	}

}
