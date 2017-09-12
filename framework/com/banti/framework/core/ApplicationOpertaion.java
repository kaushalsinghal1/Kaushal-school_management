package com.banti.framework.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.banti.framework.platform.Application;

public class ApplicationOpertaion {
	private static Map<String, Integer[]> operationMap;
	private static int token_length = 3;
	public static String OPERATION_CONFIG_FILE = "./config/Operation.csv";

	static {
		load();
	}

	private ApplicationOpertaion() {
	}

	public static boolean isAllowed(String operation) {
		if(Application.account==null){
			return false;
		}
		if (Application.account.isSuperUser()) {
			return true;
		}
		Integer list[] = operationMap.get(operation);
		if (list == null) {
			return false;
		}
		if (Application.account.isAdmin()) {
			if (list[0] == 1) {
				return true;
			}
		} else if (list[1] == 1) {
			return true;
		}
		return false;

	}

	private static void load() {
		operationMap = new HashMap<String, Integer[]>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(
					OPERATION_CONFIG_FILE)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String tokens[] = line.split(",");
				if (tokens.length != token_length) {
					continue;
				}
				Integer[] list = operationMap.get(tokens[0]);
				if (list == null) {
					list = new Integer[2];
					operationMap.put(tokens[0], list);
				}
				try {
					list[0] = Integer.parseInt(tokens[1].trim());
					list[1] = Integer.parseInt(tokens[2].trim());
				} catch (NumberFormatException ex) {
					continue;
				}
			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
