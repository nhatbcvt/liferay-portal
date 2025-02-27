/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.diff;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents a change between one or several lines. <code>changeType</code>
 * tells if the change happened in source or target. <code>lineNumber</code>
 * holds the line number of the first modified line. This line number refers to
 * a line in source or target, depending on the <code>changeType</code> value.
 * <code>changedLines</code> is a list of strings, each string is a line that is
 * already highlighted, indicating where the changes are.
 * </p>
 *
 * @author Bruno Farache
 */
public class DiffResult {

	public static final String SOURCE = "SOURCE";

	public static final String TARGET = "TARGET";

	public DiffResult(int linePos, List<String> changedLines) {
		_changedLines = changedLines;

		_lineNumber = linePos + 1;
	}

	public DiffResult(int linePos, String changedLine) {
		_lineNumber = linePos + 1;

		_changedLines = new ArrayList<>();

		_changedLines.add(changedLine);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DiffResult)) {
			return false;
		}

		DiffResult diffResult = (DiffResult)object;

		List<String> changedLines = diffResult.getChangedLines();

		if ((diffResult.getLineNumber() == _lineNumber) &&
			changedLines.equals(_changedLines)) {

			return true;
		}

		return false;
	}

	public List<String> getChangedLines() {
		return _changedLines;
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, _lineNumber);

		return HashUtil.hash(hashCode, _changedLines);
	}

	public void setChangedLines(List<String> changedLines) {
		_changedLines = changedLines;
	}

	public void setLineNumber(int lineNumber) {
		_lineNumber = lineNumber;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler((2 * _changedLines.size()) + 3);

		sb.append("Line: ");
		sb.append(_lineNumber);
		sb.append("\n");

		for (String changedLine : _changedLines) {
			sb.append(changedLine);
			sb.append("\n");
		}

		if (!_changedLines.isEmpty()) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

	private List<String> _changedLines;
	private int _lineNumber;

}