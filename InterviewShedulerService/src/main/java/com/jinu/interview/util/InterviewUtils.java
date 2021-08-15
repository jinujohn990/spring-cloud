package com.jinu.interview.util;

import java.util.List;

public class InterviewUtils {
public static <T> boolean isListNullOrEmpty(List<T> list) {
	if(list == null || list.isEmpty()) {
		return true;
	}else {
		return false;
	}
}
}
