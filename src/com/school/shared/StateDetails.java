package com.school.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.school.constant.ApplicationConstant;

public class StateDetails {
	private static final int token_length = 1;
	private List<String> states;
	private static StateDetails stateDetails = new StateDetails();

	public static StateDetails getInstance() {
		return stateDetails;

	}

	private StateDetails() {
		load();
		sortList(states);
	}

	public String[] getStatesArray() {
		return (String[])states.toArray(new String[states.size()]);

	}

	private void sortList(List<String> list) {
		Collections.sort(list);
	}

	private void load() {
		states = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(
					ApplicationConstant.STATES_CONFIG_FILE)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String tokens[] = line.split(",");
				if (tokens.length != token_length) {
					continue;
				}
				if (!states.contains(tokens[0])) {
					states.add(tokens[0]);
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
