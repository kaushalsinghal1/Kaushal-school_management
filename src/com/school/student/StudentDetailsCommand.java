package com.school.student;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.banti.framework.platform.Command;
import com.banti.framework.platform.InternalWindow;
import com.school.console.SchoolMain;
import com.school.student.module.StudentModuleSchema;

public class StudentDetailsCommand extends Command {

	private static Map<String, InternalWindow> windowMap;
	private static final String KEY = "S_KEY";

	public StudentDetailsCommand(StudentModuleSchema moduleSchema) {
		super(moduleSchema.STUDENT_DETAILS_NAME);
		setLoginNeeds(true);
		windowMap = new HashMap<String, InternalWindow>();
	}

	public static void showStudentSearchDetails() {
		if (windowMap.containsKey(KEY)) {
			InternalWindow window = windowMap.get(KEY);
			window.toFront();
			if(window instanceof StudentDetailsList){
				((StudentDetailsList) window).showSearchDialog();
			}
		} else {
			StudentDetailsList detailsList = new StudentDetailsList(true);
			detailsList.addInternalFrameListener(new InternalFrameAdapter() {

				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					super.internalFrameClosed(e);
					windowMap.remove(KEY);
				}
			});
			detailsList.setFramesize(SchoolMain.Frame);
			detailsList.setVisible(true);
			windowMap.put(KEY, detailsList);
		}
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		if (windowMap.containsKey(KEY)) {
			InternalWindow window = windowMap.get(KEY);
			window.toFront();
		} else {
			StudentDetailsList detailsList = new StudentDetailsList();
			detailsList.addInternalFrameListener(new InternalFrameAdapter() {

				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					super.internalFrameClosed(e);
					windowMap.remove(KEY);
				}
			});
			detailsList.setFramesize(SchoolMain.Frame);
			detailsList.setVisible(true);
			windowMap.put(KEY, detailsList);
		}
	}

}
